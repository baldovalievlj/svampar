package com.example.plugins

import com.example.service.convertDatabaseUrl
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.flywaydb.core.Flyway

fun Application.configureMigrations() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val jdbcConfig = config.property("ktor.database.jdbcURL").getString()
    println("Configuring migrations with: $jdbcConfig")
    val (user, databaseConfig) = convertDatabaseUrl(jdbcConfig)
    val flyway = Flyway.configure()
        .dataSource(databaseConfig, user.first, user.second)
        .load()

    flyway.migrate()
}