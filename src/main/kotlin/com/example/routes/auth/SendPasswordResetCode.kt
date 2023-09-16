package com.example.routes.auth

import com.example.data.request.SendPasswordResetCodeRequest
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.sendPasswordResetCode(
    authService: AuthService
) {
    post<AuthRoutes.SendPasswordResetCodeRoute> {
        val sendPasswordResetCodeRequest = call.receive<SendPasswordResetCodeRequest>()

        authService.sendPasswordResetCode(sendPasswordResetCodeRequest.email)

        call.respond(
            HttpStatusCode.OK,
            SuccessfulResponse<Unit>(message = "Password reset code sent successfully.")
        )
    }
}