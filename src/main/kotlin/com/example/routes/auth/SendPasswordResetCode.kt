@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.model.User
import com.example.data.response.ApiResponse
import com.example.exceptions.UserDoesNotExistException
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.sendPasswordResetCode(
    userService: UserService
) {
    authenticate {
        post<AuthRoutes.SendPasswordResetCodeRoute> {
            val user = call.principal<User>() ?: throw UserDoesNotExistException

            userService.sendPasswordResetCode(user.email)

            call.respond(
                HttpStatusCode.OK,
                ApiResponse<Unit>(true, "Password reset code sent successfully.")
            )
        }
    }
}