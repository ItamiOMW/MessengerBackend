package com.example.data.mapper

import com.example.data.database.exposed.entity.UserEntity
import com.example.data.database.exposed.table.Users
import com.example.data.response.SimpleUserResponse
import com.example.data.model.users.UpdateUser
import com.example.data.model.users.User
import org.jetbrains.exposed.sql.ResultRow
import java.time.ZoneOffset

fun ResultRow.toUser(): User {

    return User(
        id = this[Users.id].value,
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
        passwordChangeCode = this[Users.passwordChangeCode],
        isPasswordChangeAllowed = this[Users.isPasswordChangeAllowed],
        isOnline = this[Users.isOnline],
        lastActivity = this[Users.lastActivity],
        messagesPermission = this[Users.messagesPermission]
    )
}

fun UserEntity.toUser(): User {

    return User(
        id = this.id.value,
        email = this.email,
        hashPassword = this.hashPassword,
        fullName = this.fullName,
        username = this.username,
        bio = this.bio,
        profilePictureUrl = this.profilePictureUrl,
        lastActivity = this.lastActivity,
        isOnline = this.isOnline,
        isActive = this.isActive,
        isAdmin = this.isAdmin,
        isPasswordChangeAllowed = this.isPasswordChangeAllowed,
        emailVerificationCode = this.emailVerificationCode,
        passwordResetCode = this.passwordResetCode,
        passwordChangeCode = this.passwordChangeCode,
        messagesPermission = this.messagesPermission,
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
    passwordChangeCode = this.passwordChangeCode,
    isPasswordChangeAllowed = this.isPasswordChangeAllowed,
    isOnline = this.isOnline,
    lastActivity = this.lastActivity
)


fun User.toSimpleUserResponse(): SimpleUserResponse = SimpleUserResponse(
    id = this.id,
    fullName = this.fullName,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl,
    isOnline = this.isOnline,
    lastActivity = this.lastActivity.toEpochSecond(ZoneOffset.UTC),
)