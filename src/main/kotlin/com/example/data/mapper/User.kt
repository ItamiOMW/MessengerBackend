package com.example.data.mapper

import com.example.data.database.table.Users
import com.example.data.model.UpdateUser
import com.example.data.model.User
import com.example.data.response.MyUserResponse
import com.example.data.response.SimpleUserResponse
import org.jetbrains.exposed.sql.ResultRow
import java.time.ZoneOffset

fun ResultRow?.toUser(): User? {

    if (this == null) {
        return null
    }

    return User(
        id = this[Users.id],
        email = this[Users.email],
        hashPassword = this[Users.hashPassword],
        fullName = this[Users.fullName],
        username = this[Users.username],
        bio = this[Users.bio],
        profilePictureUrl = this[Users.profilePictureUrl],
        isActive = this[Users.isActive],
        isAdmin = this[Users.isAdmin],
        emailVerificationCode = this[Users.emailVerificationCode],
        passwordResetCode = this[Users.passwordResetCode],
        isPasswordResetAllowed = this[Users.isPasswordResetAllowed],
        lastActivity = this[Users.lastActivity]
    )
}


fun User.toUpdateUser(): UpdateUser = UpdateUser(
    profilePictureUrl = this.profilePictureUrl,
    hashPassword = this.hashPassword,
    fullName = this.fullName,
    username = this.username,
    bio = this.bio,
    isActive = this.isActive,
    emailVerificationCode = this.emailVerificationCode,
    passwordResetCode = this.passwordResetCode,
    isPasswordResetAllowed = this.isPasswordResetAllowed
)

fun User.toMyUserResponse(): MyUserResponse = MyUserResponse(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    username = this.username,
    bio = this.bio,
    profilePictureUrl = this.profilePictureUrl,
    isAdmin = this.isAdmin,
    isActive = this.isActive,
    isPasswordResetAllowed = this.isPasswordResetAllowed,
)

fun User.toSimpleUserResponse(): SimpleUserResponse = SimpleUserResponse(
    id = this.id,
    fullName = this.fullName,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl,
    lastActivity = this.lastActivity.toEpochSecond(ZoneOffset.UTC)
)