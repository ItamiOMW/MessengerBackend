package com.example.exceptions

import io.ktor.http.*

// Custom Auth Exceptions

data class UserAlreadyExistsException(override val message: String = "A user with this email already exists.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.UserAlreadyExists.code,
    httpStatusCode = HttpStatusCode.Conflict
)

data class UserDoesNotExistException(override val message: String = "A user with this email does not exist.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.UserDoesNotExist.code,
    httpStatusCode = HttpStatusCode.NotFound
)

data class InvalidLoginCredentialsException(override val message: String = "Invalid email or password.") : AppException(
    message = "Invalid email or password.",
    exceptionCode = ExceptionCode.InvalidLoginCredentials.code,
    httpStatusCode = HttpStatusCode.Unauthorized
)

data class InvalidEmailException(override val message: String = "Invalid email.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.InvalidEmail.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

data class InvalidPasswordException(override val message: String = "Invalid password") : AppException(
    message = message,
    exceptionCode = ExceptionCode.InvalidPassword.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

data class UserIsNotActiveException(override val message: String = "User is not active.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.UserNotActive.code,
    httpStatusCode = HttpStatusCode.Forbidden
)

data class PasswordResetNotAllowedException(override val message: String = "Password reset is not allowed.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.PasswordResetNotAllowed.code,
    httpStatusCode = HttpStatusCode.Forbidden
)

data class InvalidVerificationCodeException(override val message: String = "Invalid verification code.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.InvalidVerificationCode.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

data class InvalidPasswordResetCodeException(override val message: String = "Invalid password reset code.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.InvalidPasswordResetCode.code,
    httpStatusCode = HttpStatusCode.BadRequest
)