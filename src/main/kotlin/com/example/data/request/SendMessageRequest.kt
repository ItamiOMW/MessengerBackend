package com.example.data.request

import com.example.data.model.chat.MessageType

data class SendMessageRequest(
    val chatId: Int,
    val text: String? = null,
    val type: MessageType = MessageType.MESSAGE,
)
