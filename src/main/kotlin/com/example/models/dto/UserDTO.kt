package com.example.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val orders: List<OrderDTO>
)