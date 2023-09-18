package com.example.routes.user

import com.example.data.response.SuccessfulResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.searchUsers(
    userService: UserService
) {
    authenticate {
        get<UserRoutes.SearchForUsers> { route ->
            val userId = call.userId()
            val query = call.parameters["query"] ?: ""

            val users = userService.searchForUsersByUsername(userId, query)

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successful.",
                    data = users
                )
            )
        }
    }
}