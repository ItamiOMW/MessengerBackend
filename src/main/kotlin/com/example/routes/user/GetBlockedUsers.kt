package com.example.routes.user


import com.example.data.response.SuccessfulResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.getBlockedUsers(userService: UserService) {
    authenticate {
        get<UserRoutes.BlockedUsers> { route ->
            val userId = call.userId()

            val users = userService.getBlockedUsers(userId)

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