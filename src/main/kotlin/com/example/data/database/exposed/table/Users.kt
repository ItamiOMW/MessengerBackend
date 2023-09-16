package com.example.data.database.exposed.table

import com.example.data.model.users.MessagesPermission
import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime


object Users : IntIdTable() {
    val email = varchar("email", 255).uniqueIndex()
    val hashPassword = varchar("hash_password", 255)

    val fullName = varchar("full_name", 60)
    val username = varchar("username", 32).nullable().uniqueIndex()
    val bio = varchar("bio", 100).nullable()
    val profilePictureUrl = varchar("profile_picture", 255).nullable()

    val emailVerificationCode = integer("email_verification_code").nullable()
    val passwordChangeCode = integer("password_change_code").nullable()
    val passwordResetCode = integer("password_reset_code").nullable()

    val isActive = bool("is_active").default(false)
    val isAdmin = bool("is_admin").default(false)
    val isPasswordChangeAllowed = bool("is_password_change_allowed").default(false)
    val isOnline = bool("is_online").default(false)
    val lastActivity = datetime("last_activity").default(getCurrentDateTime())
    val messagesPermission = enumerationByName(
        "messages_permission",
        20, MessagesPermission::class
    ).default(MessagesPermission.ANYONE)
}