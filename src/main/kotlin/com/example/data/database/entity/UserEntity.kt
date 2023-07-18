package com.example.data.database.entity

import com.example.data.database.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var email by Users.email
    var hashPassword by Users.hashPassword
    var fullName by Users.fullName
    var username by Users.username
    var bio by Users.bio
    var profilePictureUrl by Users.profilePictureUrl
    var emailVerificationCode by Users.emailVerificationCode
    var passwordResetCode by Users.passwordResetCode
    var isActive by Users.isActive
    var isAdmin by Users.isAdmin
    var isPasswordResetAllowed by Users.isPasswordResetAllowed
    var lastActivity by Users.lastActivity
    var messagesPermission by Users.messagesPermission
}