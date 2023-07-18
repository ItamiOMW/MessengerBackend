package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val fullName: String?,
    val username: String?,
    val bio: String?,
)