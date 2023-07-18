package com.example.routes.contact

import com.example.data.response.ApiResponse
import com.example.service.ContactService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.getContactRequests(contactService: ContactService) {
    authenticate {
        get<ContactRoutes.GetContactRequestsRoute> { params ->
            val userId = call.userId()

            val contactRequests = contactService.getContactRequests(userId)

            call.respond(
                HttpStatusCode.Created,
                ApiResponse(
                    successful = true,
                    message = "Successfully got contact request.",
                    exceptionCode = null,
                    data = contactRequests
                )
            )
        }
    }
}