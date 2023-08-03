package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: MyUserResponse
)
