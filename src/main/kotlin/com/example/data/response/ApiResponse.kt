package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val successful: Boolean,
    val message: String? = null, // Message displayed on the client's UI
    val exceptionCode: String? = null, // Specific code that describes the exception for the client.
    val data: T? = null
)

//Example:
//  ApiResponse<T>(
//      val successful = false,
//      val message = "Account not activated", // Message displayed on the client's UI
//      val exceptionCode = 403.1, // Specific code that describes the exception for the client. In here 403.1 == Account not activated
//      val data: T? = null
//     )
