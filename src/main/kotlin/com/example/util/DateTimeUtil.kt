package com.example.util

import java.time.Clock
import java.time.LocalDateTime


fun getCurrentDateTime(): LocalDateTime {
    return LocalDateTime.now(Clock.systemUTC())
}