package com.example.models

import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

class UserPrincipal (
    val username: String
): Principal {

}