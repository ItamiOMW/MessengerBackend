@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.request.RegisterRequest
import com.example.data.response.ApiResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.register(
    userService: UserService
) {
    post<AuthRoutes.UserRegisterRoute> {

        val registerRequest = call.receive<RegisterRequest>()

        userService.createUserAndSendVerificationCode(registerRequest)

        call.respond(
            HttpStatusCode.Created,
            ApiResponse<Unit>(true, "User created successfully, confirm email.")
        )
    }
}

