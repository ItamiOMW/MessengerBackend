package com.example.routes.auth

import com.example.data.request.LoginRequest
import com.example.data.response.ApiResponse
import com.example.data.response.AuthResponse
import com.example.service.AuthService
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.login(
    authService: AuthService,
    userService: UserService
) {
    post<AuthRoutes.UserLoginRoute> {
        val loginRequest = call.receive<LoginRequest>()

        val userToToken = authService.loginUser(loginRequest)

        val user = userService.getMyUser(userToToken.first.id)
        val token = userToToken.second

        call.respond(
            HttpStatusCode.OK,
            ApiResponse(
                successful = true,
                message = "Successfully logged in.",
                data = AuthResponse(token, user)
            )
        )
    }

}