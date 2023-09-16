package com.example.data.database.exposed.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UsersSeenMessage: Table() {
    val messageId = reference("message_id", Messages.id, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(messageId, userId, name = "PK_UsersSeenMessage")
}