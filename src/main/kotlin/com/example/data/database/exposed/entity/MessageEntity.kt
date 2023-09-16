package com.example.data.database.exposed.entity

import com.example.data.database.exposed.table.Messages
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MessageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageEntity>(Messages)

    var chat by ChatEntity referencedOn Messages.chatId
    var author by UserEntity referencedOn Messages.authorId
    var text by Messages.text
    var type by Messages.type
    var createdAt by Messages.createdAt
    var updatedAt by Messages.updatedAt
}
