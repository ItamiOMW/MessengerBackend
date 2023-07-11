package com.example

import com.example.data.database.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


const val API_VERSION = "/api/v1"

@Suppress("unused")
fun Application.module() {
    DatabaseFactory.init()

    configureSerialization()
    configureKoin()
    configureResources()
    configureSockets()
    configureAuthentication()
    configureRequestValidation()
    configureRouting()
}
