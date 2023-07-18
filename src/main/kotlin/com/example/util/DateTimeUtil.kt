package com.example.util

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset


fun getCurrentDateTime(): LocalDateTime {
    return LocalDateTime.now(Clock.systemUTC())
}

fun LocalDateTime.toLong(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}