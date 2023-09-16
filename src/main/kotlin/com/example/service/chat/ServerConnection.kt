package com.example.service.chat

import io.ktor.websocket.*

data class ServerConnection(
    val userId: Int,
    val session: WebSocketSession,
)