package com.example.models.dto

import com.example.models.dto.OrderItemDTO
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class OrderDTO(
    val id: Int,
    val user: UserDTO,
    @Contextual
    val dateCreated: LocalDateTime,
    val details: String,
    val items: List<OrderItemDTO>
)
