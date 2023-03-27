package com.example.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

class TokenProviderService(private val config: ApplicationConfig) {
    suspend fun createToken(username: String): String {
        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()
        val audience = config.property("jwt.audience").getString()
        return JWT.create().withSubject("Authorization")
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + 6000000))
            .sign(Algorithm.HMAC256(secret))
    }
}