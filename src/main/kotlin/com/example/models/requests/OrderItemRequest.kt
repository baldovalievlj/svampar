package com.example.models.requests

data class OrderItemRequest (
    val type: Int,
    val amount: Double,
    val price: Double,
    val comment: String
)
