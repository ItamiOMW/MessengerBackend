package com.example.data.database.exposed.table

import com.example.data.model.chat.ChatType
import org.jetbrains.exposed.dao.id.IntIdTable

object Chats : IntIdTable("chats") {
    val name = varchar("chat_name", 50).nullable()
    val type = enumerationByName("chat_type", 20, ChatType::class)
    val chatPictureUrl = varchar("chat_picture_url", 255).nullable()
    val isActive = bool("is_active").default(false) //Chat will not be shown unless isActive == true
}