package com.example.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.configureMigrations() {
    val databaseConfig = environment.config.config("ktor.database")
    val flyway = Flyway.configure()
        .dataSource(databaseConfig.property("jdbcURL").getString(), null, null)
        .load()

    flyway.migrate()
}