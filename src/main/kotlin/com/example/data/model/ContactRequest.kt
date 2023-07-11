package com.example.data.model

import com.example.util.Constants

data class ContactRequest(
    val id: Int = Constants.UNKNOWN_ID,
    val senderId: Int,
    val recipientId: Int,
    val status: ContactRequestStatus
)
