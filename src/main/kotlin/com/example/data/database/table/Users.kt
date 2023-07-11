package com.example.data.database.table

import com.example.util.getCurrentDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime


object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val hashPassword = varchar("hash_password", 255)

    val fullName = varchar("full_name", 60)
    val username = varchar("username", 32).nullable().uniqueIndex()
    val bio = varchar("bio", 100).nullable()
    val profilePictureUrl = varchar("profile_picture", 255).nullable()

    val emailVerificationCode = integer("email_verification_code").nullable()
    val passwordResetCode = integer("password_reset_code").nullable()

    val isActive = bool("is_active").default(false)
    val isAdmin = bool("is_admin").default(false)
    val isPasswordResetAllowed = bool("is_password_reset_allowed").default(false)
    val lastActivity = datetime("datetime").default(getCurrentDateTime())

    override val primaryKey = PrimaryKey(id)
}