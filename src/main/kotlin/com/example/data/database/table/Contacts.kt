package com.example.data.database.table

import org.jetbrains.exposed.sql.Table

object Contacts: Table() {
    val firstUserId = integer("first_user_id").references(Users.id)
    val secondUserId = integer("second_user_id").references(Users.id)

    override val primaryKey = PrimaryKey(firstUserId, secondUserId)
}