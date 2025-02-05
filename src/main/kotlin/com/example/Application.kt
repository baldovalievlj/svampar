package com.example

import com.example.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

import java.util.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, module = Application::module).start(wait = true)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val logger = LoggerFactory.getLogger("Main")
    logger.debug("Starting to configure the app")
    configureEnvironment()
    val config = HoconApplicationConfig(ConfigFactory.load())
    configureSecurity(config)
    configureMigrations(config)
    configureDatabase(config)
    configureSerialization()
    configureRouting()
    configureDependencyInjection(config)
    logger.debug("Configuration is completed")
}
