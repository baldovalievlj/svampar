package com.example.models.response

import com.example.models.dto.OrderDTO

data class OrderPaged(
    val orders: List<OrderDTO>,
    override val totalCount: Int
) : Pagination