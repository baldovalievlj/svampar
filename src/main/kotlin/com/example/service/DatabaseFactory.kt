package com.example.service

import com.example.models.CategoryTable
import com.example.models.OrderItemTable
import com.example.models.OrderTable
import com.example.models.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driver = config.property("ktor.database.driverClassName").getString()
        val url = config.property("ktor.database.jdbcURL").getString()
        val username = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()
        val defaultDatabase = config.property("ktor.database.database").getString()
        val connectionPool = HikariDataSource(HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = "$url/$defaultDatabase?user=$username&password=$password"
            maximumPoolSize = 15
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        })
        val database = Database.connect(connectionPool)
        transaction() {
            SchemaUtils.createMissingTablesAndColumns(UserTable, OrderTable, OrderItemTable, CategoryTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}