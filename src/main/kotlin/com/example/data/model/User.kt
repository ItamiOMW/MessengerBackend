package com.example.data.model

import com.example.util.Constants
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = Constants.UNKNOWN_ID,
    val email: String,
    val hashedPassword: String,

    val fullName: String,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null,

    val isActive: Boolean = false,
    val isAdmin: Boolean = false,
    val isPasswordResetAllowed: Boolean = false,

    val emailVerificationCode: Int? = null,
    val passwordResetCode: Int? = null,
): Principal
