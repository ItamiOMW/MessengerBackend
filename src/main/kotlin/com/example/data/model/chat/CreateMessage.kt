package com.example.data.model.chat

data class CreateMessage(
    val authorId: Int,
    val chatId: Int,
    val text: String? = null,
    val type: MessageType,
    val pictureUrls: List<String>? = null,
)
