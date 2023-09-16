package com.example.data.response

import com.example.data.model.users.MessagesPermission
import kotlinx.serialization.Serializable

@Serializable
data class MyUserResponse(
    val id: Int,
    val email: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val contactRequestsCount: Int,
    val blockedUsersCount: Int,
    val messagesPermission: MessagesPermission
)
