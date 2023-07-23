package com.example.routes

import com.example.dao.type.typesRepository
import com.example.dao.user.usersRepository
import com.example.models.requests.TypeRequest
import com.example.models.requests.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.lang.Exception

fun Route.typeRouting() {
    authenticate("auth-jwt") {
        route("/api/type") {
            get {
                val types = typesRepository.findAll()
                if (types.isNotEmpty()) {
                    call.respond(types)
                } else {
                    call.respond(HttpStatusCode.NoContent, "no_content")
                }
            }
            get("/paged") {
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val types = typesRepository.findAllPaged(limit, offset)
                call.respond(types)
            }
            get("/{id?}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    "missing_id"
                )
                val type = typesRepository.findById(id.toInt()) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    "type_not_found",
                )
                call.respond(type.toTypeDTO())
            }
            post {
                val type = call.receive<TypeRequest>()
                try {
                    val type = typesRepository.create(type)
                    call.respond(HttpStatusCode.Created, type.toTypeDTO())
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_create_type")
                }
            }
            put("/{id?}") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val request = call.receive<TypeRequest>()
                val type = typesRepository.findById(id.toInt()) ?: return@put call.respond(
                    status = HttpStatusCode.NotFound,
                    "No type with id $id",
                )
                try {
                   typesRepository.update(type, request)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_update_type")
                }
            }
            delete("/{id?}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (typesRepository.deleteById(id.toInt())) {
                    call.response.status(HttpStatusCode.Accepted)
                } else {
                    call.respondText("type_not_found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}