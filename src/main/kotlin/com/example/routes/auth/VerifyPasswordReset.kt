package com.example.routes.auth

import com.example.data.request.VerifyPasswordResetCodeRequest
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


fun Route.verifyPasswordResetCode(
    authService: AuthService
) {
    authenticate {
        post<AuthRoutes.VerifyPasswordResetRoute> {
            val userId = call.userId()
            val verifyPasswordResetCodeRequest = call.receive<VerifyPasswordResetCodeRequest>()

            authService.verifyPasswordResetCode(userId, verifyPasswordResetCodeRequest)

            call.respond(
                HttpStatusCode.OK,
                ApiResponse<Unit>(
                    successful = true,
                    message = "Successfully verified password reset code."
                )
            )
        }
    }

}