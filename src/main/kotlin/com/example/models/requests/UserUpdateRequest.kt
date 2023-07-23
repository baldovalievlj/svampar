package com.example.models.requests

import kotlinx.serialization.Serializable

data class UserUpdateRequest(
    val username: String,
    val role: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null
)

