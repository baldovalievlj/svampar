package com.example.models.requests

import kotlinx.serialization.Serializable

data class UserRequest(
    val username: String,
    val role: String = "DISTRIBUTOR",
    val password: String? = null,
    val confirmPassword: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null
)
