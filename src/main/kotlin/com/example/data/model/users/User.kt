package com.example.data.model.users

import com.example.util.Constants
import java.time.LocalDateTime

data class User(
    val id: Int = Constants.UNKNOWN_ID,
    val email: String,
    val hashPassword: String,

    val fullName: String,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null,
    val lastActivity: LocalDateTime,
    val isOnline: Boolean,

    val isActive: Boolean = false,
    val isAdmin: Boolean = false,
    val isPasswordChangeAllowed: Boolean = false,

    val emailVerificationCode: Int? = null,
    val passwordChangeCode: Int? = null,
    val passwordResetCode: Int? = null,

    val messagesPermission: MessagesPermission,
)
