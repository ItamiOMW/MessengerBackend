package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteChatRequest(
    val id: Int,
)
