package com.example.data.model

import com.example.util.Constants
import com.example.util.getCurrentDateTime
import java.time.LocalDateTime

data class ContactRequest(
    val id: Int = Constants.UNKNOWN_ID,
    val sender: User,
    val recipient: User,
    val status: ContactRequestStatus = ContactRequestStatus.PENDING,
    val createdAt: LocalDateTime = getCurrentDateTime()
)
