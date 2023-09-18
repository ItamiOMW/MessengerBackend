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


fun Route.deleteContact(contactService: ContactService) {
    authenticate {
        delete<ContactRoutes.Contact> { route ->
            val userId = call.userId()
            val contactIdToDelete = route.userId

            contactService.deleteContact(userId, contactIdToDelete)

            call.respond(
                HttpStatusCode.OK,
                SuccessfulResponse<Unit>(
                    message = "Successfully deleted contact.",
                )
            )
        }
    }

}