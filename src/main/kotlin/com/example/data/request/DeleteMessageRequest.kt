package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageRequest(
    val id: Int,
)
