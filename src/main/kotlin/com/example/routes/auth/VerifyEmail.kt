@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.request.VerifyEmailRequest
import com.example.data.response.ApiResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.verifyEmail(
    userService: UserService
) {
    post<AuthRoutes.VerifyEmailRoute> {
        val verifyEmailRequest = call.receive<VerifyEmailRequest>()

        userService.verifyEmail(verifyEmailRequest)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse<Unit>(
                successful = true,
                message = "Successfully verified email."
            )
        )
    }

}