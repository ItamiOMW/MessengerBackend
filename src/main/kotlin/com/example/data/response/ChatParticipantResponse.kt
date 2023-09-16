package com.example.data.response

import com.example.data.model.chat.ParticipantRole
import kotlinx.serialization.Serializable

@Serializable
data class ChatParticipantResponse(
    val user: SimpleUserResponse,
    val role: ParticipantRole
)
