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
    val uri = if (url.startsWith("postgres://")) {
        URI(url.substring(11)) // remove "postgres://"
    } else {
        URI(url)
    }
    val userInfo = uri.userInfo.split(":")
    val username = userInfo[0]
    val password = userInfo[1]
    val database = uri.path.substring(1)
    return "jdbc:postgresql://${uri.host}:${uri.port}/$database?user=$username&password=$password"
}