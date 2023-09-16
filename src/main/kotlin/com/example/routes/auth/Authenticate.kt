package com.example.routes.auth

import com.example.data.response.SuccessfulResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.authenticate(
    userService: UserService,
) {
    authenticate {
        get<AuthRoutes.AuthenticateRoute> {
            val userId = call.userId()

            val myUserResponse = userService.getMyUser(userId)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Authenticated successfully.",
                    data = myUserResponse
                )
            )
        }
    }

}