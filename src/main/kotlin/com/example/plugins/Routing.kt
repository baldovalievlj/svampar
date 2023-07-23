package com.example.plugins

import com.example.routes.*
import com.example.service.ConfigurationService
import com.example.service.EmailService
import com.example.service.PdfExportService
import com.example.service.TokenProviderService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.get

fun Application.configureRouting(
) {
    routing {
        userRouting()
        orderRouting()
        loginRouting()
        typeRouting()
        sellerRouting()
        configurationRouting()
        get("/api/") {
            call.respondText("Test")
        }
    }
}
