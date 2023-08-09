package com.example.models.dto

import java.math.BigDecimal

data class OrderDTO(
    val id: Int,
    val user: UserDTO,
    val seller: SellerDTO,
    val dateCreated: String,
    val details: String?,
    val items: List<OrderItemDTO>,
    val totalPrice: BigDecimal,
    val totalAmount: BigDecimal
)

