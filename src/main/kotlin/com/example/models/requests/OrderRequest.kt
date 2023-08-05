package com.example.models.requests


data class OrderRequest(
    val details: String?,
    val seller: Int,
    val date: String?,
    val items: List<OrderItemRequest>
)
