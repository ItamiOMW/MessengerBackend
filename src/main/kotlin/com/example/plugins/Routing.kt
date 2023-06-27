package com.example.plugins

import com.example.exceptions.generalStatusPages
import com.example.routes.auth.*
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(userService: UserService) {
    install(StatusPages) {
        generalStatusPages()
    }
    routing {
        get("/") {
            call.respondText("Welcome to Itami Chat!")
        }
        register(userService)
        login(userService)
        authenticate(userService)
        sendVerification(userService)
        sendPasswordResetCode(userService)
        verifyEmail(userService)
        verifyPasswordResetCode(userService)
        resetPassword(userService)
    }

}
