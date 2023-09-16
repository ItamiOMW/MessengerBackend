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


fun Route.getUsersByIds(userService: UserService) {
    authenticate {
        get<UserRoutes.GetUsersByIdsRoute> { getUserProfileRoute ->
            val userId = call.userId()
            val userIds = call.parameters.getAll("ids")?.map { it.toInt() } ?: emptyList()

            val users = userService.getUsersByIds(userId, userIds)

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