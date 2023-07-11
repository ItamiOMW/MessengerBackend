package com.example.routes.auth

import com.example.data.request.ResetPasswordRequest
import com.example.data.response.ApiResponse
import com.example.service.AuthService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.resetPassword(
    authService: AuthService
) {
    authenticate {
        post<AuthRoutes.ResetPasswordRoute> {
            val userId = call.userId()
            val resetPasswordRequest = call.receive<ResetPasswordRequest>()

            authService.changePassword(userId, resetPasswordRequest)

            call.respond(
                HttpStatusCode.OK,
                ApiResponse<Unit>(
                    successful = true,
                    message = "Successfully changed password."
                )
            )
        }
    }

}