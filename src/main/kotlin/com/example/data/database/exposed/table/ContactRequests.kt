package com.example.data.database.exposed.table

import com.example.data.model.ContactRequestStatus
import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object ContactRequests : IntIdTable("contact_request") {
    val senderId = reference("sender_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val recipientId = reference("recipient_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val status = enumerationByName("status", 20, ContactRequestStatus::class).default(ContactRequestStatus.PENDING)
    val createdAt = datetime("created_at").default(getCurrentDateTime())
}