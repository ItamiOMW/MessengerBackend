package com.example.data.response

data class ProfileResponse(
    val userId: Long,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isOwnProfile: Boolean,
    val isContact: Boolean,
    val canSendMessage: Boolean,
    val isBlockedByMe: Boolean,
    val isBlockedByUser: Boolean,
    val enableNotifications: Boolean,
    val lastActivity: Long,
)
