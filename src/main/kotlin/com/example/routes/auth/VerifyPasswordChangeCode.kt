package com.example.routes.auth

import com.example.data.request.VerifyPasswordChangeCodeRequest
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.verifyPasswordChangeCode(
    authService: AuthService
) {
    authenticate {
        post<AuthRoutes.VerifyPasswordChangeRoute> {
            val userId = call.userId()
            val verifyPasswordChangeCodeRequest = call.receive<VerifyPasswordChangeCodeRequest>()

            authService.verifyPasswordChangeCode(userId, verifyPasswordChangeCodeRequest)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse<Unit>(
                    message = "Successfully verified password change code."
                )
            )
        }
    }

}