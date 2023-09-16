package com.example.data.response

import com.example.data.model.chat.ChatType
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: Int,
    val name: String?,
    val chatType: ChatType,
    val chatPictureUrl: String? = null,
    val participants: List<ChatParticipantResponse>,
    val lastMessage: MessageResponse? = null,
    val unreadMessagesCount: Int,
)
