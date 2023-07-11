package com.example.data.mapper

import com.example.data.database.table.ContactRequests
import com.example.data.model.ContactRequest
import com.example.data.response.ContactRequestResponse
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow?.toContactRequest(): ContactRequest? {
    if (this == null) {
        return null
    }

    return ContactRequest(
        id = this[ContactRequests.id],
        senderId = this[ContactRequests.senderId],
        recipientId = this[ContactRequests.recipientId],
        status = this[ContactRequests.status]
    )
}

fun ContactRequest.toContactRequestResponse(): ContactRequestResponse = ContactRequestResponse(
    id = this.id,
    senderId = this.senderId,
    recipientId = this.recipientId
)



