package com.example.routes.contact

import com.example.data.response.SuccessfulResponse
import com.example.service.ContactService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.sendContactRequest(contactService: ContactService) {
    authenticate {
        post<ContactRoutes.SendContactRequestRoute> { params ->
            val userId = call.userId()
            val recipientId = params.id

            val contactRequestResponse = contactService.createContactRequest(userId, recipientId)

            call.respond(
                HttpStatusCode.Created,
                SuccessfulResponse(
                    message = "Successfully send contact request.",
                    data = contactRequestResponse
                )
            )
        }
    }
}