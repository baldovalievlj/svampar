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
        val (user, databaseUrl) = convertDatabaseUrl(url)
        val connectionPool = HikariDataSource(HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = "$databaseUrl?user=${user.first}&password=${user.second}"
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

fun convertDatabaseUrl(url: String): Pair<User, String>{
    println("Converting jdbcConfig: $url")
    val userInfo = url.substringAfter("postgres://").substringBefore("@")
    val username = userInfo.substringBefore(":")
    val password = userInfo.substringAfter(":")
    val dbUrl = "jdbc:postgresql://${url.substringAfter("@")}"
    println("Converted url: $dbUrl")
    return User(username, password) to dbUrl
}

typealias User = Pair<String,String>