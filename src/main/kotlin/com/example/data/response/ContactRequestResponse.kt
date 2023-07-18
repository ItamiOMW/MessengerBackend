package com.example.data.response

import com.example.data.model.ContactRequestStatus
import com.example.data.model.SimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class ContactRequestResponse(
    val id: Int,
    val sender: SimpleUser,
    val recipient: SimpleUser,
    val status: ContactRequestStatus
)