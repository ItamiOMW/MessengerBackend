package com.example.routes.chat


import com.example.data.request.*
import com.example.exceptions.BadRequestException
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


fun Route.chatWebsocket(
    chatService: ChatService
) {
    authenticate {
        webSocket(ChatRoutes.CHAT_WEBSOCKET) {
            val chatId = call.parameters["chatId"]?.toInt() ?: throw BadRequestException("Chat id was not passed")
            val userId = call.userId()
            chatService.onConnectToChat(
                userId = userId,
                chatId = chatId,
                session = this
            )
            try {
                incoming.consumeEach { frame ->
                    kotlin.run {
                        when (frame) {
                            is Frame.Text -> {
                                val frameText = frame.readText()
                                println(frameText)
                                handleWebSocket(call.userId(), chatService, frameText)
                            }

                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                chatService.onDisconnectFromChat(this)
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
        WebSocketEvent.READ_MESSAGE.name -> {
            val readMessage = gson.fromJsonOrNull(json, ReadMessageRequest::class.java) ?: return
            chatService.readMessage(userId = ownUserId, readMessageRequest = readMessage)
        }

        WebSocketEvent.CREATE_CHAT.name -> {
            val createChat = gson.fromJsonOrNull(json, CreateChatRequest::class.java) ?: return
            chatService.createChat(creatorId = ownUserId, createChatRequest = createChat, null)
        }

        WebSocketEvent.DELETE_CHAT.name -> {
            val deleteChat = gson.fromJsonOrNull(json, DeleteChatRequest::class.java) ?: return
            chatService.deleteChat(userId = ownUserId, deleteChat.id)
        }

        WebSocketEvent.UPDATE_CHAT.name -> {
            //Todo implement
        }

        WebSocketEvent.SEND_MESSAGE.name -> {
            val sendMessage = gson.fromJsonOrNull(json, SendMessageRequest::class.java) ?: return
            chatService.sendMessage(userId = ownUserId, sendMessageRequest = sendMessage)
        }

        WebSocketEvent.EDIT_MESSAGE.name -> {
            val editMessage = gson.fromJsonOrNull(json, EditMessageRequest::class.java) ?: return
            chatService.editMessage(userId = ownUserId, editMessageRequest = editMessage)
        }

        WebSocketEvent.DELETE_MESSAGE.name -> {
            val deleteMessage = gson.fromJsonOrNull(json, DeleteMessageRequest::class.java) ?: return
            chatService.deleteMessage(userId = ownUserId, deleteMessageRequest = deleteMessage)
        }
    }
}