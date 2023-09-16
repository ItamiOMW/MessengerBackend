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

fun Route.getDialogChatByUserId(
    chatService: ChatService
) {
    authenticate {
        get<ChatRoutes.GetDialogChat> { route ->
            val myUserId = call.userId()
            val dialogUserId = route.userId

            val chat = chatService.getDialogChatByUsers(myUserId, dialogUserId)

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