package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class SendVerificationCodeRequest(
    val email: String
)
