package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import java.util.logging.Logger

fun Application.configureMigrations(config: ApplicationConfig) {
    val logger = Logger.getLogger("Configuration")
    val jdbcUrl = config.property("ktor.database.jdbcURL").getString()
    val user = config.property("ktor.database.user").getString()
    val password = config.property("ktor.database.password").getString()
    logger.info("Configuring migrations with URL: $jdbcUrl")
    val flyway = Flyway.configure()
        .dataSource(jdbcUrl, user, password)
        .load()

    flyway.migrate()
}