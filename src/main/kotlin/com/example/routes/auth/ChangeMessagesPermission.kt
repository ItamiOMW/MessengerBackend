package com.example.routes.auth

import com.example.data.request.ChangeMessagesPermissionRequest
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.changeMessagesPermission(
    authService: AuthService
) {
    authenticate {
        put<AuthRoutes.MessagesPermission> {
            val userId = call.userId()
            val changePasswordRequest = call.receive<ChangeMessagesPermissionRequest>()

            authService.changeMessagesPermission(userId, changePasswordRequest)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse<Unit>(
                    message = "Successfully changed password."
                )
            )
        }
    }

}