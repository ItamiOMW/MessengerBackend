package com.example.service

import com.example.authentication.TokenManager
import com.example.data.mapper.toUpdateUser
import com.example.data.model.UpdateUser
import com.example.data.model.User
import com.example.data.repository.user.UserRepository
import com.example.data.request.*
import com.example.email.EmailManager
import com.example.exceptions.*
import com.example.util.Constants
import com.example.util.checkPassword
import com.example.util.hashPassword
import kotlin.random.Random

class UserService(
    private val userRepository: UserRepository,
    private val simpleEmailManager: EmailManager,
    private val tokenManager: TokenManager
) {

    suspend fun createUserAndSendVerificationCode(registerRequest: RegisterRequest) {
        val doesUserExists = getUserByEmail(registerRequest.email) != null

        if (doesUserExists) {
            throw UserAlreadyExistsException
        }

        val verificationCode = generateCode()

        val user = User(
            email = registerRequest.email,
            hashedPassword = hashPassword(registerRequest.password),
            fullName = registerRequest.email.split("@").first(),
            emailVerificationCode = verificationCode,
            isActive = false,
            isAdmin = false
        )

        userRepository.createUser(user)

        simpleEmailManager.sendEmail(
            targetEmail = registerRequest.email,
            message = "Your verification code is $verificationCode.",
            subject = "Itami Chat verification."
        )
    }


    suspend fun loginUser(loginRequest: LoginRequest): String {
        val user = getUserByEmail(loginRequest.email) ?: throw InvalidLoginCredentialsException

        if (!user.isActive) {
            throw UserIsNotActiveException
        }

        val doesPasswordsMatch = checkPassword(loginRequest.password, user.hashedPassword)

        if (!doesPasswordsMatch) {
            throw InvalidLoginCredentialsException
        }

        return tokenManager.generateToken(user)
    }

    suspend fun sendEmailVerificationCode(sendVerificationCodeRequest: SendVerificationCodeRequest) {
        val email = sendVerificationCodeRequest.email

        val user = getUserByEmail(email) ?: throw UserDoesNotExistException

        val verificationCode = generateCode()

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(emailVerificationCode = verificationCode),
            email = email
        )

        simpleEmailManager.sendEmail(
            targetEmail = email,
            message = "Your verification code is $verificationCode.",
            subject = "Itami Chat - Verification."
        )
    }


    suspend fun verifyEmail(verifyEmailRequest: VerifyEmailRequest) {
        val user = getUserByEmail(verifyEmailRequest.email) ?: throw UserDoesNotExistException

        val enteredCode = verifyEmailRequest.code

        val actualCode = user.emailVerificationCode ?: throw InvalidVerificationCodeException

        if (enteredCode != actualCode) {
            throw InvalidVerificationCodeException
        }

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(isActive = true, emailVerificationCode = null),
            email = verifyEmailRequest.email
        )
    }


    suspend fun verifyPasswordResetCode(verifyPasswordResetCodeRequest: VerifyPasswordResetCodeRequest, email: String) {
        val user = getUserByEmail(email) ?: throw UserDoesNotExistException

        val enteredCode = verifyPasswordResetCodeRequest.code

        val actualCode = user.passwordResetCode ?: throw InvalidPasswordResetCodeException

        if (enteredCode != actualCode) {
            throw InvalidPasswordResetCodeException
        }

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(isPasswordResetAllowed = true, passwordResetCode = null),
            email = email
        )
    }


    suspend fun sendPasswordResetCode(email: String) {

        val user = getUserByEmail(email) ?: throw UserDoesNotExistException

        val verificationCode = generateCode()

        userRepository.updateUser(
            updateUser = user.toUpdateUser().copy(passwordResetCode = verificationCode),
            email = email
        )

        simpleEmailManager.sendEmail(
            targetEmail = email,
            message = "Your password reset code is $verificationCode.",
            subject = "Itami Chat - Password reset."
        )
    }


    suspend fun changePassword(email: String, resetPasswordRequest: ResetPasswordRequest) {
        val user = getUserByEmail(email) ?: throw UserDoesNotExistException

        if (!user.isPasswordResetAllowed) {
            throw PasswordResetNotAllowedException
        }

        val newHashedPassword = hashPassword(resetPasswordRequest.newPassword)

        userRepository.updateUser(
            user.toUpdateUser().copy(hashedPassword = newHashedPassword, isPasswordResetAllowed = false),
            user.email
        )
    }


    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }


    private fun generateCode(): Int {
        return Random.nextInt(Constants.MIN_CODE_VALUE, Constants.MAX_CODE_VALUE)
    }

}