package com.example.data.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object BlockedUsers: Table("blocked_users") {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val blockedUserId = reference("blocked_user_id", Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, blockedUserId)
}