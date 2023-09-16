package com.example.data.model.chat

import com.example.data.model.users.User

data class ChatParticipant(
    val user: User,
    val participantRole: ParticipantRole = ParticipantRole.MEMBER
)
