package com.example.routes.auth

import com.example.data.request.SendVerificationCodeRequest
import com.example.data.response.ApiResponse
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.sendVerification(
    authService: AuthService
) {
    post<AuthRoutes.SendVerificationCodeRoute> {

        val sendVerificationCodeRequest = call.receive<SendVerificationCodeRequest>()

        authService.sendEmailVerificationCode(sendVerificationCodeRequest)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse<Unit>(true, "Verification email sent successfully.")
        )
    }
}
