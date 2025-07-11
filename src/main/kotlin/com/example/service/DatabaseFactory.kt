package com.example.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory

object DatabaseFactory {

    val logger = LoggerFactory.getLogger("DatabaseFactory")

    fun init(config: ApplicationConfig) {
        val driver = config.property("ktor.database.driverClassName").getString()
        val url = config.property("ktor.database.jdbcURL").getString()
        val user = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()
        logger.info("Initializing database with url: $url")
        val connectionPool = HikariDataSource(HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            username = user
            this.password = password
            maximumPoolSize = 15
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        })
        Database.connect(connectionPool)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

typealias User = Pair<String,String>