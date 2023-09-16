package com.example.data.database.exposed.table

import com.example.data.model.chat.ParticipantRole
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Participants : Table("participants") {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val chatId = reference("chat_id", Chats.id, onDelete = ReferenceOption.CASCADE)
    val participantRole = enumerationByName("participant_role", 20, ParticipantRole::class)

    override val primaryKey = PrimaryKey(userId, chatId, name = "PK_Participants")
}