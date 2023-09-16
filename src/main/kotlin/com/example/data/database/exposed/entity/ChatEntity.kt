package com.example.data.database.exposed.entity

import com.example.data.database.exposed.table.Chats
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ChatEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChatEntity>(Chats)

    var name by Chats.name
    var type by Chats.type
    var chatPictureUrl by Chats.chatPictureUrl
    var isActive by Chats.isActive
}
