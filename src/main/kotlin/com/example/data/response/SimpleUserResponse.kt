package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class SimpleUserResponse(
    val id: Int,
    val fullName: String,
    val username: String?,
    val profilePictureUrl: String?,
    val lastActivity: Long
)
