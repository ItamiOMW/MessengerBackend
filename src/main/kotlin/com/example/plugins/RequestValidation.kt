package com.example.plugins

import com.example.validation.authRequestsValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authRequestsValidation()
    }
}