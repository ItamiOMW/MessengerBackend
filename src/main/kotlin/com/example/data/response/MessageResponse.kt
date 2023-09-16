package com.example.data.response

import com.example.data.model.chat.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: Int,
    val chatId: Int,
    val authorId: Int,
    val authorFullName: String,
    val authorProfilePictureUrl: String?,
    val text: String?,
    val type: MessageType,
    val isRead: Boolean,
    val usersSeenMessage: List<Int>,
    val pictureUrls: List<String>?,
    val createdAt: Long,
    val updatedAt: Long?,
)
