package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailRequest(
    val code: Int,
    val email: String
)
