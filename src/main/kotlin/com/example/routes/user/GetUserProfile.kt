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


fun Route.getUserProfile(userService: UserService) {
    authenticate {
        get<UserRoutes.GetUserProfileRoute> { getUserProfileRoute ->
            val userId = call.userId()
            val userIdToGetProfile = getUserProfileRoute.id

            val profile = userService.getProfile(userId, userIdToGetProfile)

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successfully got the user profile.",
                    data = profile
                )
            )
        }
    }
}