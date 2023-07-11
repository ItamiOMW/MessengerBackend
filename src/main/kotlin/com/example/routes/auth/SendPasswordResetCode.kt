package com.example.routes.auth

import com.example.data.response.ApiResponse
import com.example.service.AuthService
import com.example.util.userEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.sendPasswordResetCode(
    authService: AuthService
) {
    authenticate {
        post<AuthRoutes.SendPasswordResetCodeRoute> {
            val userEmail = call.userEmail()

            authService.sendPasswordResetCode(userEmail)

            call.respond(
                HttpStatusCode.OK,
                ApiResponse<Unit>(true, "Password reset code sent successfully.")
            )
        }
    }
}