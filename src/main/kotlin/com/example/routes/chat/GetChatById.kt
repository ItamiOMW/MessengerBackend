package com.example.routes.chat

import com.example.data.response.SuccessfulResponse
import com.example.service.chat.ChatService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getChatById(
    chatService: ChatService
) {
    authenticate {
        get<ChatRoutes.Chat> { route ->
            val userId = call.userId()
            val chatId = route.id

            val chat = chatService.getChatById(userId, chatId)

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successful.",
                    data = chat
                )
            )
        }
    }
}