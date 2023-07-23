package com.example.dao.type

import com.example.models.entities.TypeEntity
import com.example.models.dto.TypeDTO
import com.example.models.requests.TypeRequest
import com.example.models.response.TypePaged

interface TypeDaoFacade {
    suspend fun findAll(): List<TypeDTO>
    suspend fun findById(id: Int): TypeEntity?
    suspend fun create(request: TypeRequest): TypeEntity
    suspend fun update(type: TypeEntity, request: TypeRequest): TypeEntity
    suspend fun deleteById(id: Int): Boolean
    suspend fun findAllPaged(limit: Int, offset: Int): TypePaged
}