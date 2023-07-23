package com.example.routes

import com.example.dao.user.usersRepository
import com.example.models.requests.PasswordRequest
import io.ktor.server.routing.*
import com.example.models.requests.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.userRouting() {
    authenticate("auth-jwt") {
        route("/api/user") {
            get {
                val users = usersRepository.findAll()
                if (users.isNotEmpty()) {
                    call.respond(users.map { it.toUserDTO() })
                } else {
                    call.respond(HttpStatusCode.NoContent, "no_content")
                }
            }
            get("/paged") {
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val paged = usersRepository.findAllPaged(limit, offset)
                call.respond(paged)
            }
            get("/{id?}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    "missing_id",
                )
                val user = usersRepository.findById(id.toInt())?.toUserDTO() ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    "user_not_found",
                )
                call.respond(user)
            }
            get("/role") {
                call.principal<JWTPrincipal>()?.payload?.claims?.get("username")
                call.respond("Test")
            }
            post {
                val user = call.receive<UserRequest>()
                if (user.password == null) {
                    call.respond(HttpStatusCode.BadRequest, "no_password_error")
                }
                if (user.password != user.confirmPassword) {
                    call.respond(HttpStatusCode.BadRequest, "password_match_error")
                }
                try {
                    if (usersRepository.create(user)) {
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(status = HttpStatusCode.Conflict, "failed_to_create_user")
                    }
                } catch (e: ExposedSQLException) {
                    call.respond(HttpStatusCode.BadRequest, "user_username_unique")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "unknown_error")
                }
            }
            put("/{id?}") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val user = call.receive<UserRequest>()
                try {
                    val updated = usersRepository.updateUser(id.toInt(), user)
                    if (updated) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(status = HttpStatusCode.Conflict, "failed_to_update_user")
                    }
                } catch (e: ExposedSQLException) {
                    call.respond(HttpStatusCode.BadRequest, "user_username_unique")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "unknown_error")
                }
            }
            put("/{id?}/password") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val request = call.receive<PasswordRequest>()
                if (request.password != request.confirmPassword) {
                    call.respond(HttpStatusCode.BadRequest, "password_match_error")
                }
                try {
                    val updated = usersRepository.updateUserPassword(id.toInt(), request)
                    if (updated) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(status = HttpStatusCode.Conflict, "failed_to_update_user")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "unknown_error")
                }
            }
            delete("/{id?}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (usersRepository.deleteById(id.toInt())) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.NotFound, "user_not_found")
                }
            }
        }
    }
}