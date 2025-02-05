package com.example.service

import com.example.service.DatabaseFactory.logger
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.logging.Logger


object DatabaseFactory {

    val logger = LoggerFactory.getLogger("DatabaseFactory")

    fun init(config: ApplicationConfig) {
        val driver = config.property("ktor.database.driverClassName").getString()
        val url = config.property("ktor.database.jdbcURL").getString()
        val (user, databaseUrl) = convertDatabaseUrl(url)
        val connectionPool = HikariDataSource(HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = "$databaseUrl?user=${user.first}&password=${user.second}"
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
fun convertDatabaseUrl(url: String): Pair<User, String>{
    logger.debug("Converting jdbcConfig: $url")
    return if(url.startsWith("postgres")) {
        val userInfo = url.substringAfter("postgres://").substringBefore("@")
        val username = userInfo.substringBefore(":")
        val password = userInfo.substringAfter(":")
        val dbUrl = "jdbc:postgresql://${url.substringAfter("@")}"
        User(username, password) to dbUrl
    } else {
        User("postgres", "postgres") to url
    }

}

typealias User = Pair<String,String>