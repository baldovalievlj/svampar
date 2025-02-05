package com.example.plugins

import com.example.service.convertDatabaseUrl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import java.util.logging.Logger

fun Application.configureMigrations(config: ApplicationConfig) {
    val logger = Logger.getLogger("Configuration")
    val jdbcUrl = config.property("ktor.database.jdbcURL").getString()
    logger.info("Configuring migrations with URL: $jdbcUrl")
    val (user, databaseConfig) = convertDatabaseUrl(jdbcUrl)
    val flyway = Flyway.configure()
        .dataSource(databaseConfig, user.first, user.second)
        .load()

    flyway.migrate()
}