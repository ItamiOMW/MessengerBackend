package com.example.routes.auth

import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.deleteAccount(
    authService: AuthService,
) {
    authenticate {
        delete<AuthRoutes.AccountRoute> {
            val userId = call.userId()

            authService.deleteAccount(userId)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successfully deleted account.",
                    data = Unit
                )
            )
        }
    }
}