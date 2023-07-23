package com.example.dao.order

import com.example.models.entities.OrderEntity
import com.example.models.entities.SellerEntity
import com.example.models.entities.UserEntity
import com.example.models.dto.OrderDTO
import com.example.models.requests.OrderRequest
import com.example.models.response.OrderPaged

interface OrderDaoFacade {
    suspend fun findAll(): List<OrderDTO>
    suspend fun findAllByUser(user: UserEntity): List<OrderDTO>
    suspend fun findById(id: Int): OrderDTO?
    suspend fun findAllPaged(
        limit: Int,
        offset: Int,
        userId: Int? = null,
        startDate: String? = null,
        endDate: String? = null
    ): OrderPaged
    suspend fun create(request: OrderRequest, user: UserEntity, seller: SellerEntity): OrderEntity

}