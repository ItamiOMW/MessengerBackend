package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: MyUserResponse,
    val token: String
)
