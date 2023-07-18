package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpleUser(
    val id: Int,
    val fullName: String,
    val username: String?,
    val profilePictureUrl: String?,
    val lastActivity: Long,
)
