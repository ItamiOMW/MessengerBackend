@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.routes.auth

import com.example.data.request.SendVerificationCodeRequest
import com.example.data.response.ApiResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.sendVerification(
    userService: UserService
) {
    post<AuthRoutes.SendVerificationCodeRoute> {

        val sendVerificationCodeRequest = call.receive<SendVerificationCodeRequest>()

        userService.sendEmailVerificationCode(sendVerificationCodeRequest)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse<Unit>(true, "Verification email sent successfully.")
        )
    }
}
