package com.example.data.database.entity

import com.example.data.database.table.ContactRequests
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class ContactRequestEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ContactRequestEntity>(ContactRequests)

    var sender by UserEntity referencedOn ContactRequests.senderId
    var recipient by UserEntity referencedOn ContactRequests.recipientId
    var status by ContactRequests.status
    var createdAt by ContactRequests.createdAt
}
