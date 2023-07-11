package com.example.routes.auth

import com.example.data.mapper.toMyUserResponse
import com.example.data.response.ApiResponse
import com.example.exceptions.UserDoesNotExistException
import com.example.service.AuthService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.authenticate(
    authService: AuthService
) {
    authenticate {
        get<AuthRoutes.AuthenticateRoute> {
            val userId = call.userId()

            val user = authService.getUserById(userId) ?: throw UserDoesNotExistException()

            call.respond(
                HttpStatusCode.OK,
                ApiResponse(
                    successful = true,
                    message = "Authenticated successfully.",
                    data = user.toMyUserResponse()
                )
            )
        }
    }

}