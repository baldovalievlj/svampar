package com.example.plugins

import com.example.dao.configuration.ConfigurationRepository
import com.example.service.*
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection(config: ApplicationConfig) {
    install(Koin) {
        modules(module {
            single { HttpClient() }
            single { TokenProviderService(config) }
            single { PdfExportService() }
            single { ConfigurationRepository() }
            single { ConfigurationService(get()) }
            single { EmailService(get(), get()) }
        })
    }
}