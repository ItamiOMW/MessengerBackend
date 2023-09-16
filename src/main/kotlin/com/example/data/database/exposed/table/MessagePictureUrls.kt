package com.example.data.database.exposed.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessagePictureUrls: Table() {
    val messageId = reference("message_id", Messages.id, onDelete = ReferenceOption.CASCADE)
    val pictureUrl = varchar("image_url", 255)
}