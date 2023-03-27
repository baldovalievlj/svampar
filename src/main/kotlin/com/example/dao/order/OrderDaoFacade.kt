package com.example.dao.order

import com.example.models.OrderEntity

interface OrderDaoFacade {
    suspend fun findAllOrders(): List<OrderEntity>
    suspend fun findById(id: Int): OrderEntity?
    suspend fun findTotalForOrder(id: Int): Int?
}