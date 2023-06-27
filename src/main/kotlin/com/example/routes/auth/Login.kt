@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.request.LoginRequest
import com.example.data.response.ApiResponse
import com.example.data.response.AuthResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.login(
    userService: UserService
) {
    post<AuthRoutes.UserLoginRoute> {
        val loginRequest = call.receive<LoginRequest>()

        val token = userService.loginUser(loginRequest)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse(
                successful = true,
                message = "Successfully logged in.",
                data = AuthResponse(token)
            )
        )
    }

}