package com.example.exceptions

import io.ktor.http.*



data class ForbiddenException(override val message: String = "Forbidden.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.Forbidden.code,
    httpStatusCode = HttpStatusCode.Forbidden
)

data class BadRequestException(override val message: String = "Bad Request.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.BadRequest.code,
    httpStatusCode = HttpStatusCode.BadRequest
)

data class NotFoundException(override val message: String = "Not found.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.NotFound.code,
    httpStatusCode = HttpStatusCode.NotFound
)

data class ConflictException(override val message: String = "Conflict.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.Conflict.code,
    httpStatusCode = HttpStatusCode.Conflict
)

data class MissingArgumentsException(override val message: String = "Missing arguments.") : AppException(
    message = message,
    exceptionCode = ExceptionCode.MissingArguments.code,
    httpStatusCode = HttpStatusCode.BadRequest
)