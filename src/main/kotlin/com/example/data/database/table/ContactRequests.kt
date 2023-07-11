package com.example.data.database.table

import com.example.data.model.ContactRequestStatus
import org.jetbrains.exposed.sql.Table

object ContactRequests : Table() {
    val id = integer("id").autoIncrement()
    val senderId = integer("sender").references(Users.id)
    val recipientId = integer("recipient").references(Users.id).uniqueIndex()
    val status = enumerationByName("status", 20, ContactRequestStatus::class).default(ContactRequestStatus.PENDING)
    override val primaryKey = PrimaryKey(id)
}