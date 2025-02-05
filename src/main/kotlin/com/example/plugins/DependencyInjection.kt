package com.example.plugins

import com.example.dao.configuration.ConfigurationRepository
import com.example.service.*
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger
import org.koin.core.logger.Level

fun Application.configureDependencyInjection(config: ApplicationConfig) {
    install(Koin) {
        logger(SLF4JLogger(Level.DEBUG))
        modules(module {
            single { config }
            single { HttpClient() }
            single { TokenProviderService( get() ) }
            single { PdfExportService() }
            single { ConfigurationRepository() }
            single { ConfigurationService(get()) }
            single { EmailService(get(), get(), get()) }
        })
    }
}