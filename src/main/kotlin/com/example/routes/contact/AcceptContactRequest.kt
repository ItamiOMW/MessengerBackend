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


fun Route.acceptContactRequest(contactService: ContactService) {
    authenticate {
        post<ContactRoutes.AcceptContactRequestRoute> { params ->
            val userId = call.userId()
            val requestId = params.id

            contactService.acceptContactRequest(userId, requestId)

            call.respond(
                HttpStatusCode.Created,
                SuccessfulResponse<Unit>(
                    message = "Successfully accepted contact request.",
                )
            )
        }
    }

}