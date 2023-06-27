@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.locations.*


fun Application.configureLocations() {
    install(Locations)
}