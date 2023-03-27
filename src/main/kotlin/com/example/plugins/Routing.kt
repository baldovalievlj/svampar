package com.example.plugins

import com.example.routes.loginRouting
import com.example.routes.orderRouting
import com.example.routes.userRouting
import com.example.service.TokenProviderService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting(tokenProviderService: TokenProviderService) {
    routing {
        userRouting()
        orderRouting()
        loginRouting(tokenProviderService)
        get("/api/") {
            call.respondText("Test")
        }
    }
}
