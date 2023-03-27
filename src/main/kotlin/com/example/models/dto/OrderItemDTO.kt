package com.example.models.dto

import com.example.models.dto.CategoryDTO
import kotlinx.serialization.Serializable


@Serializable
data class OrderItemDTO(
    val id: Long,
    val order: OrderDTO,
    val item: String?,
    val category: CategoryDTO,
    val amount: Int,
    val price: Double
)