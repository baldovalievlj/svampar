package com.example.models.dto
data class OrderItemDTO(
    val id: Int,
    val comment: String?,
    val type: TypeDTO,
    val amount: Double,
    val price: Double
)