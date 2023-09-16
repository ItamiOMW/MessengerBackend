package com.example.routes.auth

import com.example.data.request.VerifyEmailRequest
import com.example.data.response.AuthResponse
import com.example.data.response.SuccessfulResponse
import com.example.service.AuthService
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.verifyEmail(
    authService: AuthService,
    userService: UserService,
) {
    post<AuthRoutes.VerifyEmailRoute> {
        val verifyEmailRequest = call.receive<VerifyEmailRequest>()

        val userToToken = authService.verifyEmail(verifyEmailRequest)

        val myUser = userService.getMyUser(userToToken.first.id)
        val token = userToToken.second

        call.respond(
            HttpStatusCode.OK,
            SuccessfulResponse(
                message = "Successfully verified email.",
                data = AuthResponse(token = token, user = myUser)
            )
        )
    }

}