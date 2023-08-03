package com.example.data.database.exposed.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Contacts: Table() {
    val firstUserId = reference("first_user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val secondUserId = reference("second_user_id", Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(firstUserId, secondUserId)
}