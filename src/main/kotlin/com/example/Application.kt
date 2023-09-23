package com.example

import com.example.data.database.exposed.DatabaseFactory
import com.example.data.database.firebase.FirebaseAdmin
import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


const val API_VERSION = "/api/v1"

@Suppress("unused")
fun Application.module() {
    DatabaseFactory.init(environment.config)
    FirebaseAdmin.init()

    configureCallLogging()
    configureSerialization()
    configureKoin()
    configureResources()
    configureSockets()
    configureAuthentication()
    configureRequestValidation()
    configureRouting()
}
