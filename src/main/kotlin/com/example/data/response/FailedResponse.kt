package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class FailedResponse(
    val message: String,
    val exceptionCode: String
)
