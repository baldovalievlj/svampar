package com.example.routes

import com.example.dao.user.usersRepository
import com.example.models.response.Authentication
import com.example.models.requests.LoginRequest
import com.example.models.requests.PasswordChangeRequest
import com.example.service.TokenProviderService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt

fun Route.loginRouting() {
    val tokenProviderService: TokenProviderService by inject()

    post("/api/login") {
        val login = call.receive<LoginRequest>()
        val user = usersRepository.findByUsername(username = login.username)
            ?.takeIf { BCrypt.checkpw(login.password, it.password) }
            ?: return@post call.respondText(
                "Wrong username or password",
                status = HttpStatusCode.Unauthorized
            )

        val token = tokenProviderService.createToken(user.username, user.role.name)
        call.respond(hashMapOf("token" to token))
    }

    authenticate("auth-jwt") {
        get("/api/authentication") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val user = usersRepository.findByUsername(username)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "user_not_found"
                )
            call.respond(
                HttpStatusCode.OK,
                Authentication(username = user.username, role = user.role)
            )
        }
        put("/api/authentication/change_password") {
            val request = call.receive<PasswordChangeRequest>()
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val user = usersRepository.findByUsername(username)
                ?: return@put call.respond(
                    HttpStatusCode.NotFound,
                    "user_not_found"
                )
            if (request.password != request.confirmPassword) {
                call.respond(HttpStatusCode.BadRequest, "password_match_error")
            }
            if (BCrypt.checkpw(request.currentPassword, user.password)) {
                val updated = usersRepository.updateUserPassword(user.id.value, request.password)
                if (updated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_update_user")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "current_password_match_error",)
            }
        }
    }

}

