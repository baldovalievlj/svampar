package com.example.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val username: String,
    val role: String = "DISTRIBUTOR",
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null
)
