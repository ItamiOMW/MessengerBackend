package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class AddChatParticipantRequest(
    val participantIds: List<Int>,
    val chatId: Int,
)
