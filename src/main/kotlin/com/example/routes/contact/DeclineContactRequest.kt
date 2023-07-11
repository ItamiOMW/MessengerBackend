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


fun Route.declineContactRequest(contactService: ContactService) {
    authenticate {
        post<ContactRoutes.DeclineContactRequestRoute> { params ->
            val userId = call.userId()
            val requestId = params.id

            contactService.declineContactRequest(userId, requestId)

            call.respond(
                HttpStatusCode.Created,
                ApiResponse<Unit>(
                    successful = true,
                    message = "Successfully declined contact request.",
                    exceptionCode = null,
                )
            )
        }
    }

}