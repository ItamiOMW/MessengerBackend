@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.mapper.toUserResponse
import com.example.data.model.User
import com.example.data.response.ApiResponse
import com.example.exceptions.UserDoesNotExistException
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.authenticate(
    userService: UserService
) {
    authenticate {
        get<AuthRoutes.AuthenticateRoute> {
            val user = call.principal<User>() ?: throw UserDoesNotExistException

            call.respond(
                HttpStatusCode.OK,
                ApiResponse(
                    successful = true,
                    message = "Authenticated successfully.",
                    data = user.toUserResponse()
                )
            )
        }
    }

}