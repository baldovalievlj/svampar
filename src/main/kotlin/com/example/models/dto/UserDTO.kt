package com.example.models.dto

import com.example.models.Role
import kotlinx.serialization.Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val role: Role,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
//    val orders: List<OrderDTO>
)