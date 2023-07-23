package com.example.dao.type

import com.example.models.entities.TypeEntity
import com.example.models.entities.TypeTable
import com.example.models.dto.TypeDTO
import com.example.models.requests.TypeRequest
import com.example.models.response.TypePaged
import com.example.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.SortOrder

class TypeDaoImpl : TypeDaoFacade {
    override suspend fun findAll(): List<TypeDTO> = dbQuery {
        TypeEntity.all().filter { !it.deleted }.toList().map { it.toTypeDTO() }
    }

    override suspend fun findAllPaged(limit: Int, offset: Int): TypePaged = dbQuery {
        val types = TypeEntity.find { TypeTable.deleted eq false }.orderBy(TypeTable.id to SortOrder.ASC)
        TypePaged(
            types = types.limit(limit, offset = offset.toLong()).toList().map { it.toTypeDTO() },
            totalCount = types.count().toInt()
        )
    }
    override suspend fun findById(id: Int): TypeEntity? = dbQuery {
        TypeEntity.findById(id)?.takeUnless { it.deleted }
    }

    override suspend fun create(request: TypeRequest): TypeEntity = dbQuery {
        TypeEntity.new {
            this.name = request.name
            this.description = request.description
            this.deleted = false
        }
    }
    override suspend fun update(type: TypeEntity, request: TypeRequest): TypeEntity = dbQuery {
        type.name = request.name
        type.description = request.description
        type
    }
    override suspend fun deleteById(id: Int): Boolean = dbQuery {
        TypeEntity.findById(id)?.deleted = true
        true
//        TypeTable.update({TypeTable.id eq id}) { TypeTable.deleted = true } > 0
    }
}

val typesRepository: TypeDaoImpl = TypeDaoImpl()