package com.example.data.model

data class UpdateUser(
    val profilePictureUrl: String?,
    val hashedPassword: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val isActive: Boolean,
    val emailVerificationCode: Int?,
    val passwordResetCode: Int?,
    val isPasswordResetAllowed: Boolean,
)
