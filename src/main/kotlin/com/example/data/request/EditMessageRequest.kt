package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class EditMessageRequest(
    val id: Int,
    val text: String,
)
