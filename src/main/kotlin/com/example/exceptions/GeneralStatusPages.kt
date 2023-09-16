package com.example.exceptions

import com.example.data.response.FailedResponse
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.lang.reflect.InvocationTargetException


fun StatusPagesConfig.generalStatusPages() {
    exception<AppException> { call, cause ->
        call.respond(
            status = cause.httpStatusCode,
            message = FailedResponse(
                message = cause.message,
                exceptionCode = cause.exceptionCode
            )
        )
    }
    exception<InvocationTargetException> { call, invocationTargetException ->
        invocationTargetException.cause?.printStackTrace()
    }
    exception<RequestValidationException> { call, cause ->
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = FailedResponse(
                message = "Validation Exception.",
                exceptionCode = ExceptionCode.BadRequest.code
            )
        )
    }
    exception<Exception> { call, cause ->
        cause.printStackTrace()
        call.respond(
            status = HttpStatusCode.InternalServerError,
            message = FailedResponse(
                message = "Error occurred.",
                exceptionCode = ExceptionCode.InternalServerError.code
            )
        )
    }
}