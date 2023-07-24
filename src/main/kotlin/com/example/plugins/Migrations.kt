package com.example.plugins

import com.example.service.convertDatabaseUrl
import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.configureMigrations() {
    val jdbcConfig = environment.config.property("ktor.database.jdbcURL").getString()
    println("Configuring migrations with: $jdbcConfig")
    val (user, databaseConfig) = convertDatabaseUrl(jdbcConfig)
    val flyway = Flyway.configure()
        .dataSource(databaseConfig, user.first, user.second)
        .load()

    flyway.migrate()
}