package com.example.data.response

import com.example.data.model.contact.ContactRequestStatus
import kotlinx.serialization.Serializable

@Serializable
data class ContactRequestResponse(
    val id: Int,
    val sender: SimpleUserResponse,
    val recipient: SimpleUserResponse,
    val status: ContactRequestStatus
)