package com.example.util

import com.example.exceptions.MissingArgumentsException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun ApplicationCall.userId(): Int =
    this.principal<JWTPrincipal>()?.getClaim("id", Int::class) ?: throw MissingArgumentsException()

fun ApplicationCall.userEmail(): String =
    this.principal<JWTPrincipal>()?.getClaim("email", String::class) ?: throw MissingArgumentsException()