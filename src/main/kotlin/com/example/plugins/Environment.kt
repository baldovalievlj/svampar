package com.example.plugins

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.slf4j.LoggerFactory

fun Application.configureEnvironment() {
    val logger = LoggerFactory.getLogger("Configuration")
    val dotFile = when(System.getenv("PROFILE")) {
        "prod" -> {
            logger.info("Using production env file")
            ".env.production"
        }
        else -> {
            logger.info("Using default env file")
            ".env"
        }
    }

    val dotenv = dotenv {
        directory = "."
        filename = dotFile
        ignoreIfMissing = true
    }

    dotenv.entries().forEach {
        System.setProperty(it.key, it.value)
    }
}