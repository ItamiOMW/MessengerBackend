package com.example.data.model.chat

import com.example.data.model.users.User
import com.example.util.Constants
import com.example.util.getCurrentDateTime
import java.time.LocalDateTime

data class Message(
    val id: Int = Constants.UNKNOWN_ID,
    val text: String? = null,
    val type: MessageType,
    val pictureUrls: List<String>? = null,
    val userIdsSeenMessage: List<Int> = emptyList(),
    val createdAt: LocalDateTime = getCurrentDateTime(),
    val updatedAt: LocalDateTime? = null,
    val author: User,
    val chatId: Int
)
