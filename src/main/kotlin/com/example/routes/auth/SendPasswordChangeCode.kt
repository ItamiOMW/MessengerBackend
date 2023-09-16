package com.example.routes.auth

import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import com.example.util.userEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.sendPasswordChangeCode(
    authService: AuthService
) {
    authenticate {
        post<AuthRoutes.SendPasswordChangeCodeRoute> {
            val userEmail = call.userEmail()

            authService.sendPasswordChangeCode(userEmail)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse<Unit>(message = "Password change code sent successfully.")
            )
        }
    }
}