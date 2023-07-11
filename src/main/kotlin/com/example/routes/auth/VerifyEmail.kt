package com.example.routes.auth

import com.example.data.request.VerifyEmailRequest
import com.example.data.response.ApiResponse
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.verifyEmail(
    authService: AuthService
) {
    post<AuthRoutes.VerifyEmailRoute> {
        val verifyEmailRequest = call.receive<VerifyEmailRequest>()

        authService.verifyEmail(verifyEmailRequest)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse<Unit>(
                successful = true,
                message = "Successfully verified email."
            )
        )
    }

}