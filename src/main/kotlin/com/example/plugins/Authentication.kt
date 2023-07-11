package com.example.plugins

import com.example.authentication.JwtTokenManager
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {

    val jwtTokenManager = JwtTokenManager()

    install(Authentication) {
        jwt {
            verifier(jwtTokenManager.verifier)
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("email").asString() != null &&
                    jwtCredential.payload.getClaim("id").asLong() != null
                ) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}
