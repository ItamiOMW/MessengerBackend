package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteChatParticipantRequest(
    val participantId: Int,
    val chatId: Int,
)
