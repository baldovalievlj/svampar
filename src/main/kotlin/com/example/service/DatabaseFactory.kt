package com.example.service

import com.example.models.entities.TypeTable
import com.example.models.entities.OrderItemTable
import com.example.models.entities.OrderTable
import com.example.models.entities.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
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

fun convertDatabaseUrl(databaseUrl: String): String {
    val uri = URI(databaseUrl)
    val username = uri.userInfo.split(":")[0]
    val password = uri.userInfo.split(":")[1]
    val dbUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?user=$username&password=$password"
    return dbUrl
}