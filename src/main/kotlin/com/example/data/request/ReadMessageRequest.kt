package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageRequest(
    val id: Int,
)
