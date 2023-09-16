package com.example.data.database.exposed.table

import com.example.data.model.chat.MessageType
import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object Messages : IntIdTable() {
    val chatId = reference("chat_id", Chats.id, onDelete = ReferenceOption.CASCADE)
    val authorId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val text = varchar("text", 400).nullable()
    val type = enumerationByName<MessageType>("message_type", 20).default(MessageType.MESSAGE)
    val createdAt = datetime("created_at").default(getCurrentDateTime())
    val updatedAt = datetime("updated_at").nullable()
}