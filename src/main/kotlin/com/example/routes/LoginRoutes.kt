package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.user.usersRepository
import com.example.models.Login
import com.example.service.TokenProviderService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.loginRouting(tokenProviderService: TokenProviderService) {

    post("/api/login") {
        val login = call.receive<Login>()
        // Check username and password
        val user = usersRepository.findByUsername(username = login.username)
            ?.takeIf { BCrypt.checkpw(login.password, it.password) }
            ?: return@post call.respondText(
                "Wrong username or password",
                status = HttpStatusCode.Forbidden
            )

        // ...

        val token = tokenProviderService.createToken(user.username)
        call.respond(hashMapOf("token" to token))
    }

    authenticate("auth-jwt") {
        get("/hello") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }

}

