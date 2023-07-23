package com.example.routes

import com.example.dao.user.usersRepository
import com.example.models.response.Authentication
import com.example.models.requests.LoginRequest
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
        // Check username and password
        val user = usersRepository.findByUsername(username = login.username)
            ?.takeIf { BCrypt.checkpw(login.password, it.password) }
            ?: return@post call.respondText(
                "Wrong username or password",
                status = HttpStatusCode.Unauthorized
            )

        // ...

        val token = tokenProviderService.createToken(user.username, user.role.name)
//        call.sessions.set(LoginSession(token))
//        call.respond(HttpStatusCode.OK)
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
            call.respond(HttpStatusCode.OK,
                Authentication(username = user.username, role = user.role)
            )
        }
    }

}

