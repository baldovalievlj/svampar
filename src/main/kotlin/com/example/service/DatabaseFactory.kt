package com.example.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.net.URI


object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driver = config.property("ktor.database.driverClassName").getString()
        val url = config.property("ktor.database.jdbcURL").getString()
        println("Configuring database with: $url")
        val databaseUrl = convertDatabaseUrl(url)
        val connectionPool = HikariDataSource(HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = databaseUrl
            maximumPoolSize = 15
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        })
        val database = Database.connect(connectionPool)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

fun convertDatabaseUrl(url: String): String {
    println("Converting jdbcConfig: $url")
    val uri = URI(url.substringAfter("postgres://"))
    val username = uri.userInfo.substringBefore(":")
    val password = uri.userInfo.substringAfter(":")
    val jdbc = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
    println("Converted url: $jdbc")
    return jdbc
}