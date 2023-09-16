package com.example.routes.auth

import com.example.data.request.RegisterRequest
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.register(
    authService: AuthService
) {
    post<AuthRoutes.UserRegisterRoute> {

        val registerRequest = call.receive<RegisterRequest>()

        authService.createUserAndSendVerificationCode(registerRequest)

        call.respond(
            HttpStatusCode.Created,
            SuccessfulResponse<Unit>(message = "User created successfully, confirm email.")
        )
    }
}

