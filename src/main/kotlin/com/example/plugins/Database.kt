package com.example.plugins

import com.example.service.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.configureDatabase(config: ApplicationConfig) {
    DatabaseFactory.init(config)
}