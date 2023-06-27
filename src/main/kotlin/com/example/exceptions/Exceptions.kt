package com.example.exceptions

import io.ktor.http.*


object UserAlreadyExistsException : AppException(
    message = "A user with this email already exists.",
    exceptionCode = ExceptionCode.UserAlreadyExists.code,
    httpStatusCode = HttpStatusCode.Conflict
)

object UserDoesNotExistException : AppException(
    message = "A user with this email already exists.",
    exceptionCode = ExceptionCode.UserAlreadyExists.code,
    httpStatusCode = HttpStatusCode.Conflict
)

object InvalidLoginCredentialsException : AppException(
    message = "Invalid email or password.",
    exceptionCode = ExceptionCode.InvalidLoginCredentials.code,
    httpStatusCode = HttpStatusCode.Unauthorized
)

object InvalidEmailException : AppException(
    message = "Invalid email.",
    exceptionCode = ExceptionCode.InvalidEmail.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

object InvalidPasswordException : AppException(
    message = "Invalid password. Length should be greater than 7",
    exceptionCode = ExceptionCode.InvalidPassword.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

object UserIsNotActiveException : AppException(
    message = "User is not active.",
    exceptionCode = ExceptionCode.UserNotActive.code,
    httpStatusCode = HttpStatusCode.Forbidden
)

object PasswordResetNotAllowedException : AppException(
    message = "Password reset is not allowed.",
    exceptionCode = ExceptionCode.PasswordResetNotAllowed.code,
    httpStatusCode = HttpStatusCode.Forbidden
)

object InvalidVerificationCodeException : AppException(
    message = "Invalid verification code.",
    exceptionCode = ExceptionCode.InvalidVerificationCode.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

object InvalidPasswordResetCodeException : AppException(
    message = "Invalid password reset code.",
    exceptionCode = ExceptionCode.InvalidPasswordResetCode.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

object MissingArgumentsException : AppException(
    message = "Missing arguments.",
    exceptionCode = ExceptionCode.MissingArguments.code,
    httpStatusCode = HttpStatusCode.BadRequest
)