package com.example.routes.chat


import com.example.data.response.SuccessfulResponse
import com.example.service.chat.ChatService
import com.example.util.userId
import com.google.cloud.storage.Bucket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.deleteChat(
    chatService: ChatService,
    bucket: Bucket
) {
    authenticate {
        delete<ChatRoutes.Chat> { route ->
            val userId = call.userId()
            val chatId = route.id

            val chat = chatService.getChatById(userId, chatId)
            chatService.deleteChat(userId, chatId)
            chat.chatPictureUrl?.let { url ->
                bucket.storage.delete(url)
            }

            call.respond(
                status = HttpStatusCode.OK,
                SuccessfulResponse(
                    message = "Successful.",
                    data = Unit
                )
            )
        }
    }
}