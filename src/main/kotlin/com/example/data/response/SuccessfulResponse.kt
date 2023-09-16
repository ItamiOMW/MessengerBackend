package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class SuccessfulResponse<T>(
    val message: String,
    val data: T? = null
)
