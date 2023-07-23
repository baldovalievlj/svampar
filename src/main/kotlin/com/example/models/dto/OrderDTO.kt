package com.example.models.dto

import java.time.LocalDateTime

data class OrderDTO(
    val id: Int,
    val user: UserDTO,
    val seller: SellerDTO,
    val dateCreated: LocalDateTime,
    val details: String,
    val items: List<OrderItemDTO>,
    val totalPrice: Double,
    val totalAmount: Double
)

