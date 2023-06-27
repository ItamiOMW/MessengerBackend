package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyPasswordResetCodeRequest(
    val code: Int,
)
