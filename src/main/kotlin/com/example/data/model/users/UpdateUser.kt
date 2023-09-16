package com.example.data.model.users

import java.time.LocalDateTime

data class UpdateUser(
    val profilePictureUrl: String?,
    val hashPassword: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val isActive: Boolean,
    val isOnline: Boolean,
    val lastActivity: LocalDateTime,
    val emailVerificationCode: Int?,
    val passwordChangeCode: Int?,
    val passwordResetCode: Int?,
    val isPasswordChangeAllowed: Boolean,
)
