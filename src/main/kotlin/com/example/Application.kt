package com.example

import com.example.data.database.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.service.UserService
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


const val API_VERSION = "/api/v1"

@Suppress("unused")
fun Application.module() {
    DatabaseFactory.init()

    val userService: UserService by inject()

    configureSerialization()
    configureKoin()
    configureLocations()
    configureSockets()
    configureAuthentication(userService)
    configureRequestValidation()
    configureRouting(userService)
}
