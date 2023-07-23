package com.example.models.response

import com.example.models.Role
import kotlinx.serialization.Serializable

@Serializable
data class Authentication(
    val username: String,
    val role: Role
)
