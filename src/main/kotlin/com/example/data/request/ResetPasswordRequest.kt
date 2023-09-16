package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val passwordResetCode: Int,
    val newPassword: String,
)
