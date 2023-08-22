package com.example.plugins

import com.example.dao.configuration.ConfigurationRepository
import com.example.service.*
import io.ktor.client.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger
import org.koin.core.logger.Level

fun Application.configureDependencyInjection() {
    install(Koin) {
        logger(SLF4JLogger(Level.DEBUG))
        modules(module {
            single { HttpClient() }
            single { TokenProviderService() }
            single { PdfExportService() }
            single { ConfigurationRepository() }
            single { ConfigurationService(get()) }
            single { EmailService(get(), get()) }
        })
    }
}