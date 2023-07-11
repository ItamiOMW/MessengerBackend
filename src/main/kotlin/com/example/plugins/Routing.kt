package com.example.plugins

import com.example.exceptions.generalStatusPages
import com.example.routes.auth.*
import com.example.routes.contact.acceptContactRequest
import com.example.routes.contact.cancelContactRequest
import com.example.routes.contact.declineContactRequest
import com.example.routes.contact.sendContactRequest
import com.example.service.AuthService
import com.example.service.ContactService
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(StatusPages) {
        generalStatusPages()
    }

    val authService: AuthService by inject()
    val contactService: ContactService by inject()

    routing {
        get("/") {
            call.respondText("Welcome to Itami Chat!")
        }
        register(authService)
        login(authService)
        authenticate(authService)
        sendVerification(authService)
        sendPasswordResetCode(authService)
        verifyEmail(authService)
        verifyPasswordResetCode(authService)
        resetPassword(authService)

        sendContactRequest(contactService)
        acceptContactRequest(contactService)
        declineContactRequest(contactService)
        cancelContactRequest(contactService)
    }

}
