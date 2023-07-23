package com.example.models.requests

import com.example.models.dto.OrderItemDTO
import com.example.models.dto.UserDTO
import java.time.LocalDateTime

data class OrderRequest(
    val details: String,
    val seller: Int,
    val items: List<OrderItemRequest>
)
