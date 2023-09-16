package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyPasswordChangeCodeRequest(
    val code: Int,
)
