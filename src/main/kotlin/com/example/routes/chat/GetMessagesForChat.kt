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


fun Route.getMessagesForChat(
    chatService: ChatService
) {
    authenticate {
        get<ChatRoutes.GetMessagesForChat> { route ->
            val userId = call.userId()
            val chatId = route.id
            val page = route.page
            val pageSize = route.pageSize

            val messages = chatService.getMessagesForChat(userId, chatId, page, pageSize)

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successful.",
                    data = messages
                )
            )
        }
    }
}