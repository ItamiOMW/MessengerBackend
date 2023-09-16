package com.example.routes.auth

import com.example.data.request.ResetPasswordRequest
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.resetPassword(
    authService: AuthService
) {
    post<AuthRoutes.ResetPasswordRoute> {
        val resetPasswordRequest = call.receive<ResetPasswordRequest>()

        authService.resetPassword(resetPasswordRequest)

        call.respond(
            HttpStatusCode.OK,
            SuccessfulResponse<Unit>(
                message = "Successfully changed password."
            )
        )
    }

}