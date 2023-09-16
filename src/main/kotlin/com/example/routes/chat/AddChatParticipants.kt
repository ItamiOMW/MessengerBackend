package com.example.routes.chat


import com.example.data.response.SuccessfulResponse
import com.example.service.chat.ChatService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.addChatParticipants(
    chatService: ChatService,
) {
    authenticate {
        post<ChatRoutes.ChatParticipants> { route ->
            val userId = call.userId()
            val chatId = route.id
            val userIds = call.parameters.getAll("ids")?.map { it.toInt() } ?: emptyList()

            val participants = chatService.addChatParticipants(userId, userIds, chatId)

            call.respond(
                status = HttpStatusCode.Created,
                SuccessfulResponse(
                    message = "Successful.",
                    data = participants
                )
            )
        }
    }
}