package com.example.validation

import com.example.data.request.*
import com.example.exceptions.*
import com.example.util.Constants
import io.ktor.server.plugins.requestvalidation.*
import java.util.regex.Pattern


fun RequestValidationConfig.authRequestsValidation() {
    validate<RegisterRequest> { registerRequest ->

        val email = registerRequest.email.trim()
        val password = registerRequest.password.trim()

        if (email.isEmpty()) throw InvalidEmailException("Empty email.")
        if (!email.isEmailValid()) throw InvalidEmailException()

        if (password.isEmpty()) throw InvalidPasswordException("Empty Password.")
        if (password.length < 8) throw InvalidPasswordException("Short password. Length should be greater than 7.")

        ValidationResult.Valid
    }
    validate<LoginRequest> { loginRequest ->
        val email = loginRequest.email.trim()
        val password = loginRequest.password.trim()

        if (email.isEmpty()) throw InvalidEmailException("Empty email.")
        if (password.isEmpty()) throw InvalidPasswordException("Empty Password.")

        ValidationResult.Valid
    }
    validate<VerifyEmailRequest> { verifyEmailRequest ->
        val code = verifyEmailRequest.code
        val email = verifyEmailRequest.email

        if (email.isEmpty()) throw InvalidEmailException("Empty email.")

        if (code < Constants.MIN_CODE_VALUE || code > Constants.MAX_CODE_VALUE) {
            throw InvalidVerificationCodeException()
        }

        ValidationResult.Valid
    }
    validate<VerifyPasswordChangeCodeRequest> { verifyPasswordResetCodeRequest ->
        val code = verifyPasswordResetCodeRequest.code

        if (code < Constants.MIN_CODE_VALUE || code > Constants.MAX_CODE_VALUE) {
            throw InvalidVerificationCodeException()
        }

        ValidationResult.Valid
    }
    validate<SendVerificationCodeRequest> { sendVerificationCodeRequest ->
        val email = sendVerificationCodeRequest.email

        if (email.isEmpty()) throw InvalidEmailException("Empty email.")

        ValidationResult.Valid
    }
    validate<ChangePasswordRequest> { resetPasswordRequest ->
        val password = resetPasswordRequest.newPassword

        if (password.isEmpty()) throw InvalidPasswordException("Empty Password.")
        if (password.length < 8) throw InvalidPasswordException("Short password. Length should be greater than 7.")

        ValidationResult.Valid
    }
    validate<SendPasswordResetCodeRequest> { sendPasswordResetCodeRequest ->
        val email = sendPasswordResetCodeRequest.email

        if (!email.isEmailValid()) {
            throw InvalidEmailException()
        }

        ValidationResult.Valid
    }
    validate<ResetPasswordRequest> { resetPasswordRequest ->
        val password = resetPasswordRequest.newPassword
        val code = resetPasswordRequest.passwordResetCode

        if (password.isEmpty()) throw InvalidPasswordException("Empty Password.")
        if (password.length < 8) throw InvalidPasswordException("Short password. Length should be greater than 7.")

        if (code < Constants.MIN_CODE_VALUE || code > Constants.MAX_CODE_VALUE) {
            throw InvalidVerificationCodeException()
        }

        ValidationResult.Valid
    }
}

fun String.isEmailValid() = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
).matcher(this).matches()