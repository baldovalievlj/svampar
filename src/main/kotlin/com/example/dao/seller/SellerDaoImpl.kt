package com.example.dao.seller

import com.example.models.dto.SellerDTO
import com.example.models.entities.SellerEntity
import com.example.models.entities.SellerTable
import com.example.models.requests.SellerRequest
import com.example.models.response.SellerPaged
import com.example.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.SortOrder

class SellerDaoImpl : SellerDaoFacade {
    override suspend fun findAll(): List<SellerDTO> = dbQuery {
        SellerEntity.all().orderBy(SellerTable.id to SortOrder.ASC).filter { !it.deleted }.map { it.toSellerDTO() }
    }

    override suspend fun findAllPaged(limit: Int, offset: Int): SellerPaged = dbQuery {
        val sellers = SellerEntity.find { SellerTable.deleted eq false }.orderBy(SellerTable.id to SortOrder.ASC)
        SellerPaged(
            sellers = sellers.limit(limit, offset = offset.toLong()).toList().map { it.toSellerDTO() },
            totalCount = sellers.count().toInt()
        )
    }
    override suspend fun findById(id: Int): SellerEntity? = dbQuery {
        SellerEntity.findById(id)?.takeUnless { it.deleted }
    }

    override suspend fun create(request: SellerRequest): SellerEntity = dbQuery {
        SellerEntity.new {
            this.name = request.name
            this.socialSecurityNumber = request.socialSecurityNumber
            this.address = request.address
            this.phoneNumber = request.phoneNumber
            this.email = request.email
            this.additionalInfo = request.additionalInfo
            this.deleted = false
        }
    }

    override suspend fun update(seller: SellerEntity, request: SellerRequest): SellerEntity = dbQuery {
        seller.name = request.name
        seller.socialSecurityNumber = request.socialSecurityNumber
        seller.address = request.address
        seller.phoneNumber = request.phoneNumber
        seller.email = request.email
        seller.additionalInfo = request.additionalInfo
        seller
    }
    override suspend fun deleteById(id: Int): Boolean = dbQuery {
        SellerEntity.findById(id)?.deleted = true
        true
//        SellerTable.deleteWhere { SellerTable.id eq id } > 0
    }
}

val sellersRepository: SellerDaoImpl = SellerDaoImpl()