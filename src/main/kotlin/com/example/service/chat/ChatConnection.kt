package com.example.service.chat

import io.ktor.websocket.*

data class ChatConnection(
    val userId: Int,
    val chatId: Int,
    val session: WebSocketSession,
)
