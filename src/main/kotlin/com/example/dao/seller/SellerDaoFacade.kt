package com.example.dao.seller

import com.example.models.entities.SellerEntity
import com.example.models.dto.SellerDTO
import com.example.models.requests.SellerRequest
import com.example.models.response.SellerPaged

interface SellerDaoFacade {
    suspend fun findAll(): List<SellerDTO>
    suspend fun findAllPaged(limit: Int, offset: Int): SellerPaged
    suspend fun findById(id: Int): SellerEntity?
    suspend fun create(request: SellerRequest): SellerEntity
    suspend fun update(seller: SellerEntity, request: SellerRequest): SellerEntity
    suspend fun deleteById(id: Int): Boolean
}