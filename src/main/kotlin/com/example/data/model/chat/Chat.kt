package com.example.data.model.chat

import com.example.util.Constants

data class Chat(
    val id: Int = Constants.UNKNOWN_ID,
    val name: String? = null,
    val type: ChatType,
    val chatPictureUrl: String? = null,
    val participants: List<ChatParticipant>,
    val isActive: Boolean = false,
)
