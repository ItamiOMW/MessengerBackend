package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: Int,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isOwnProfile: Boolean,
    val isContact: Boolean,
    val canSendMessage: Boolean,
    val isBlockedByMe: Boolean,
    val isBlockedByUser: Boolean,
    val lastActivity: Long,
)
