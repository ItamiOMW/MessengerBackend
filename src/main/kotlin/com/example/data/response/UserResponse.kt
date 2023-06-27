package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val email: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isActive: Boolean,
    val isAdmin: Boolean,
    val isPasswordResetAllowed: Boolean,
)
