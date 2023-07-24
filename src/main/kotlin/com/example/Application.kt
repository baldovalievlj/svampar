package com.example

import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File

import java.util.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        routing {
            staticFiles("/", File("m-client/dist/m-client"), index = "index.html") {
                default("/index.html")
            }
        }
    }.start(wait = true)
}
//    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val logger = LoggerFactory.getLogger("Main")
    logger.debug("Starting to configure the app")
    configureSecurity(environment.config)
    configureMigrations()
    configureDatabase(environment.config)
    configureSerialization()
    configureRouting()
    configureDependencyInjection(environment.config)
    logger.debug("Using port: ${environment.config.port}")
    logger.debug("Configuration is completed")
}

fun Application.myApplicationModule() {

}
