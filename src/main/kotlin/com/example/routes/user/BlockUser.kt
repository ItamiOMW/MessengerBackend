package com.example.routes.user

import com.example.data.response.ApiResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.blockUser(userService: UserService) {
    authenticate {
        post<UserRoutes.BlockUserRoute> { blockUserRoute ->
            val userId = call.userId()
            val userIdToBlock = blockUserRoute.id

            userService.blockUser(userId, userIdToBlock)

            call.respond(
                status = HttpStatusCode.OK,
                ApiResponse<Unit>(
                    successful = true,
                    message = "Successfully blocked the user.",
                )
            )
        }
    }
}