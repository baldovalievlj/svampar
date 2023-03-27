package com.example.routes

import com.example.dao.user.usersRepository
import io.ktor.server.routing.*
import com.example.models.*
import com.example.models.requests.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.mindrot.jbcrypt.BCrypt

fun Route.userRouting() {

    route("/user") {
        get {
            val users = usersRepository.findAllUsers()
            if (users.isNotEmpty()) {
                call.respond(users)
            } else {
                call.respondText("No users created yet.", status = HttpStatusCode.OK)
            }
        }
        get("/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val user = usersRepository.findById(id.toInt()) ?: return@get call.respondText(
                "No user with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        post {
            val user = call.receive<UserRequest>()
            if (usersRepository.createUser(user)) {
                call.respondText("User created", status = HttpStatusCode.Created)
            } else {
                call.respondText("Failed to create user", status = HttpStatusCode.Conflict)
            }
        }
        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (usersRepository.deleteById(id.toInt())) {
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
    }

    authenticate("auth-jwt") {
        get("/user/role") {
            println(call.principal<JWTPrincipal>()?.payload?.claims?.get("username"))
            call.respond("Test")
        }
    }

}