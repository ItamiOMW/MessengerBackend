package com.example.data.request

import com.example.data.model.chat.ChatType
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    val name: String?,
    val type: ChatType,
    val participantIds: List<Int>,
)
