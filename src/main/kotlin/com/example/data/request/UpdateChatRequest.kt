package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateChatRequest(
    val name: String?,
)
