package com.example.exceptions

import io.ktor.http.*

open class AppException(
    override val message: String,
    val exceptionCode: String,
    val httpStatusCode: HttpStatusCode,
): Exception()
