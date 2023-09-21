package com.example.service

import com.example.authentication.TokenManager
import com.example.data.mapper.toUpdateUser
import com.example.data.model.users.MessagesPermission
import com.example.data.model.users.User
import com.example.data.repository.user.UserRepository
import com.example.data.request.*
import com.example.email.EmailManager
import com.example.exceptions.*
import com.example.util.Constants
import com.example.util.checkPassword
import com.example.util.getCurrentDateTime
import com.example.util.hashPassword
import kotlin.random.Random

class AuthService(
    private val userRepository: UserRepository,
    private val simpleEmailManager: EmailManager,
    private val tokenManager: TokenManager
) {

    suspend fun changeMessagesPermission(userId: Int, changeMessagesPermissionRequest: ChangeMessagesPermissionRequest) {
        userRepository.changeMessagesPermission(userId, changeMessagesPermissionRequest.messagesPermission)
    }

    suspend fun deleteAccount(userId: Int) {
        userRepository.deleteUser(userId)
    }

    suspend fun createUserAndSendVerificationCode(registerRequest: RegisterRequest) {
        val doesUserExists = getUserByEmail(registerRequest.email) != null

        if (doesUserExists) {
            throw UserAlreadyExistsException()
        }

        val verificationCode = generateCode()

        val user = User(
            email = registerRequest.email,
            hashPassword = hashPassword(registerRequest.password),
            fullName = registerRequest.email.split("@").first(),
            emailVerificationCode = verificationCode,
            isActive = false,
            isAdmin = false,
            isOnline = false,
            lastActivity = getCurrentDateTime(),
            messagesPermission = MessagesPermission.ANYONE,
        )

        userRepository.createUser(user)

        simpleEmailManager.sendEmail(
            targetEmail = registerRequest.email,
            message = "Your verification code is $verificationCode.",
            subject = "Itami Chat - Email verification."
        )
    }


    suspend fun loginUser(loginRequest: LoginRequest): Pair<User, String> {
        val user = getUserByEmail(loginRequest.email) ?: throw UnauthorizedException()
        if (!user.isActive) {
            throw UserIsNotActiveException()
        }

        val doesPasswordsMatch = checkPassword(loginRequest.password, user.hashPassword)
        if (!doesPasswordsMatch) {
            throw UnauthorizedException()
        }

        val token = tokenManager.generateToken(user)

        return user to token
    }


    suspend fun sendEmailVerificationCode(sendVerificationCodeRequest: SendVerificationCodeRequest) {
        val email = sendVerificationCodeRequest.email
        val user = getUserByEmail(email) ?: throw UserDoesNotExistException()
        val verificationCode = generateCode()

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(emailVerificationCode = verificationCode),
            id = user.id
        )

        simpleEmailManager.sendEmail(
            targetEmail = email,
            message = "Your verification code is $verificationCode.",
            subject = "Itami Chat - Email verification."
        )
    }


    suspend fun verifyEmail(verifyEmailRequest: VerifyEmailRequest): Pair<User, String> {
        val user = getUserByEmail(verifyEmailRequest.email) ?: throw UserDoesNotExistException()
        val enteredCode = verifyEmailRequest.code
        val actualCode = user.emailVerificationCode ?: throw InvalidVerificationCodeException()

        if (enteredCode != actualCode) {
            throw InvalidVerificationCodeException()
        }

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(isActive = true, emailVerificationCode = null),
            id = user.id
        )

        val token = tokenManager.generateToken(user)

        return user to token
    }


    suspend fun verifyPasswordChangeCode(
        userId: Int,
        verifyPasswordChangeCodeRequest: VerifyPasswordChangeCodeRequest
    ) {
        val user = getUserById(userId) ?: throw UserDoesNotExistException()
        val enteredCode = verifyPasswordChangeCodeRequest.code
        val actualCode = user.passwordChangeCode ?: throw InvalidVerificationCodeException()

        if (enteredCode != actualCode) {
            throw InvalidVerificationCodeException()
        }

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(isPasswordChangeAllowed = true, passwordChangeCode = null),
            id = userId
        )
    }


    suspend fun sendPasswordChangeCode(email: String) {
        val user = getUserByEmail(email) ?: throw UserDoesNotExistException()
        val verificationCode = generateCode()

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(passwordChangeCode = verificationCode),
            id = user.id
        )

        simpleEmailManager.sendEmail(
            targetEmail = email,
            message = "Your password change code is $verificationCode.",
            subject = "Itami Chat - Password change."
        )
    }

    suspend fun sendPasswordResetCode(email: String) {
        val user = getUserByEmail(email) ?: throw UserDoesNotExistException()
        val verificationCode = generateCode()

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(passwordResetCode = verificationCode),
            id = user.id
        )

        simpleEmailManager.sendEmail(
            targetEmail = email,
            message = "Your password reset code is $verificationCode.",
            subject = "Itami Chat - Password reset."
        )
    }

    suspend fun changePassword(userId: Int, changePasswordRequest: ChangePasswordRequest) {
        val user = getUserById(userId) ?: throw UserDoesNotExistException()

        if (!user.isPasswordChangeAllowed) {
            throw PasswordResetNotAllowedException()
        }

        val newHashedPassword = hashPassword(changePasswordRequest.newPassword)

        userRepository.updateUser(
            user.toUpdateUser().copy(hashPassword = newHashedPassword, isPasswordChangeAllowed = false),
            userId
        )
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        val user = getUserByEmail(resetPasswordRequest.email) ?: throw UserDoesNotExistException()

        if (resetPasswordRequest.passwordResetCode != user.passwordResetCode) {
            throw InvalidVerificationCodeException()
        }

        val newHashedPassword = hashPassword(resetPasswordRequest.newPassword)

        userRepository.updateUser(
            user.toUpdateUser().copy(hashPassword = newHashedPassword, passwordResetCode = null),
            user.id
        )
    }

    private suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    private suspend fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    private fun generateCode(): Int {
        return Random.nextInt(Constants.MIN_CODE_VALUE, Constants.MAX_CODE_VALUE)
    }

}