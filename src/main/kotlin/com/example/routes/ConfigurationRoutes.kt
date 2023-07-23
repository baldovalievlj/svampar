package com.example.routes

import com.example.dao.configuration.ConfigurationRepository
import com.example.models.Configuration
import com.example.service.ConfigurationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.lang.Exception

fun Route.configurationRouting() {
    val repository: ConfigurationRepository by inject()
    val service: ConfigurationService by inject()

    authenticate("auth-jwt") {
        route("/api/configuration") {
            get {
                call.respond(service.getAll())
            }
            post {
                val config = call.receive<Configuration>()
                try {
                    repository.createOrUpdateConfiguration(config)
                    call.respond(HttpStatusCode.Created)
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_edit_configuration")
                }
            }
        }
    }
}