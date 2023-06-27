package com.example.exceptions

import com.example.data.response.ApiResponse
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun StatusPagesConfig.generalStatusPages() {
    exception<AppException> { call, cause ->
        call.respond(
            cause.httpStatusCode,
            ApiResponse<Unit>(
                successful = false,
                message = cause.message,
                exceptionCode = cause.exceptionCode,
            )
        )
    }
    exception<RequestValidationException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ApiResponse(
                successful = false,
                message = "Validation exception.",
                data = cause.reasons
            )
        )
    }
    exception<Exception> { call, cause ->
        call.respond(
            HttpStatusCode.InternalServerError,
            ApiResponse<Unit>(false, "Error occurred.")
        )
    }
}