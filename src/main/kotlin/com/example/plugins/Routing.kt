package com.example.plugins

import com.example.exceptions.generalStatusPages
import com.example.routes.auth.*
import com.example.routes.contact.*
import com.example.routes.user.blockUser
import com.example.routes.user.getUserProfile
import com.example.routes.user.unblockUser
import com.example.routes.user.updateProfile
import com.example.service.AuthService
import com.example.service.BlockService
import com.example.service.ContactService
import com.example.service.UserService
import com.google.cloud.storage.Bucket
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
    val userService: UserService by inject()
    val blockService: BlockService by inject()

    val bucket: Bucket by inject()

    routing {
        get("/") {
            call.respondText("Welcome to Itami Chat!")
        }

        //Auth
        register(authService)
        login(authService, userService)
        authenticate(userService)
        sendVerification(authService)
        sendPasswordResetCode(authService)
        verifyEmail(authService, userService)
        verifyPasswordResetCode(authService)
        resetPassword(authService)

        //Contacts
        sendContactRequest(contactService)
        acceptContactRequest(contactService)
        declineContactRequest(contactService)
        cancelContactRequest(contactService)
        getContactRequests(contactService)

        //Users
        updateProfile(userService, bucket)
        getUserProfile(userService)
        blockUser(blockService)
        unblockUser(blockService)
    }

}
