package com.example.util

import com.example.exceptions.MissingArgumentsException
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun ApplicationCall.userId() = this.principal<UserIdPrincipal>("id") ?: throw MissingArgumentsException