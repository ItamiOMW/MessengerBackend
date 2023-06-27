package com.example.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.example.authentication.JwtTokenManager
import com.example.service.UserService
import io.ktor.server.application.*

fun Application.configureAuthentication(userService: UserService) {

    val jwtTokenManager = JwtTokenManager()

    install(Authentication) {
        jwt {
            verifier(jwtTokenManager.verifier)
            validate { jwtCredential ->
                jwtCredential.payload.getClaim("email").asString()?.let { email ->
                    userService.getUserByEmail(email)
                }
            }
        }
    }
}
