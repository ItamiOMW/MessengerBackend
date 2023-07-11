package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ContactRequestResponse(
    val id: Int,
    val senderId: Int,
    val recipientId: Int,
)