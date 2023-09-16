package com.example.routes.chat

import com.example.data.request.CreateChatRequest
import com.example.data.request.DeleteChatRequest
import com.example.service.chat.ChatService
import com.example.service.chat.WebSocketEvent
import com.example.util.fromJsonOrNull
import com.example.util.userId
import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.java.KoinJavaComponent.inject


fun Route.chatsWebsocket(
    chatService: ChatService
) {
    authenticate {
        webSocket(ChatRoutes.CHATS_WEBSOCKET) {
            val userId = call.userId()
            chatService.onConnect(userId, this)
            try {
                incoming.consumeEach { frame ->
                    kotlin.run {
                        when (frame) {
                            is Frame.Text -> {
                                val frameText = frame.readText()
                                handleWebSocket(userId, chatService, frameText)
                            }

                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                chatService.onDisconnect(call.userId(), this)
            }
        }
    }
}

private suspend fun handleWebSocket(
    ownUserId: Int,
    chatService: ChatService,
    frameText: String,
) {
    val gson by inject<Gson>(Gson::class.java)
    val delimiterIndex = frameText.indexOf("#")
    if (delimiterIndex == -1) {
        println("No delimiter found")
        return
    }
    val type = frameText.substring(0, delimiterIndex)
    val json = frameText.substring(delimiterIndex + 1, frameText.length)
    when (type) {
        WebSocketEvent.CREATE_CHAT.name -> {
            val createChat = gson.fromJsonOrNull(json, CreateChatRequest::class.java) ?: return
            chatService.createChat(creatorId = ownUserId, createChatRequest = createChat, null)
        }

        WebSocketEvent.DELETE_CHAT.name -> {
            val deleteChat = gson.fromJsonOrNull(json, DeleteChatRequest::class.java) ?: return
            chatService.deleteChat(userId = ownUserId, deleteChat.id)
        }

        WebSocketEvent.UPDATE_CHAT.name -> {

        }
    }
}