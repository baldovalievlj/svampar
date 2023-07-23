package com.example.dao.order

import com.example.models.dto.OrderDTO
import com.example.models.entities.*
import com.example.models.requests.OrderRequest
import com.example.models.response.OrderPaged
import com.example.service.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import java.time.LocalDate
import java.time.LocalDateTime

class OrderDaoImpl : OrderDaoFacade {
    override suspend fun findAll(): List<OrderDTO> = dbQuery {
        OrderEntity.all().orderBy(OrderTable.id to SortOrder.DESC).toList().map { it.toOrderDTO() }
    }

    override suspend fun findAllByUser(user: UserEntity): List<OrderDTO> = dbQuery {
        val orders = OrderTable.leftJoin(OrderItemTable).leftJoin(TypeTable).leftJoin(UserTable)
            .select { OrderTable.user eq user.id }
        OrderEntity.wrapRows(orders).toList().map { it.toOrderDTO() }
    }

    //    override suspend fun findAllPaged(limit: Int, offset: Int, userId: Int? = null, startDate: LocalDate? = null, endDate: LocalDate? = null, type: String? = null): OrderPaged = dbQuery {
//        val orders = OrderEntity.all().orderBy(OrderTable.id to SortOrder.DESC)
//        OrderPaged(
//            orders = orders.limit(limit, offset = offset.toLong()).toList().map { it.toOrderDTO() },
//            totalCount = orders.count().toInt()
//        )
//    }
    override suspend fun findAllPaged(
        limit: Int,
        offset: Int,
        userId: Int?,
        startDate: String?,
        endDate: String?
    ): OrderPaged = dbQuery {
        val conditions = mutableListOf<Op<Boolean>>()

         userId?.let {
            conditions.add(OrderTable.user eq it)
        }

        startDate?.let {
            val startDateTime = LocalDate.parse(it).atStartOfDay()
            conditions.add(OrderTable.dateCreated greaterEq startDateTime)
            if (endDate != null) {
                val endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59)
                conditions.add(OrderTable.dateCreated lessEq endDateTime)
            } else {
                val endDateTime = LocalDate.parse(it).atTime(23, 59, 59)
                conditions.add(OrderTable.dateCreated lessEq endDateTime)
            }
        }
        endDate?.let {
            val endDateTime = LocalDate.parse(it).atTime(23, 59, 59)
            conditions.add(OrderTable.dateCreated lessEq endDateTime)
        }

        val query = if (conditions.isEmpty()) {
            OrderEntity.all()
        } else {
            OrderEntity.find(conditions.reduce { acc, op -> acc and op })
        }
        val pagedOrders = query.orderBy(OrderTable.dateCreated to SortOrder.DESC)
            .limit(limit, offset = offset.toLong()).map { it.toOrderDTO() }.toList()

        OrderPaged(orders = pagedOrders, totalCount = query.count().toInt())
    }

    override suspend fun findById(id: Int): OrderDTO? = dbQuery {
        OrderEntity.findById(id)?.toOrderDTO()
    }

    override suspend fun create(request: OrderRequest, user: UserEntity, seller: SellerEntity): OrderEntity = dbQuery {
        val order = OrderEntity.new {
            this.user = user
            this.seller = seller
            this.dateCreated = LocalDateTime.now()
            this.details = request.details
        }
        val orderItems = request.items.map {
            val type = TypeEntity.findById(it.type) ?: throw BadRequestException("Type not found")
            OrderItemEntity.new {
                this.order = order
                this.comment = it.comment
                this.type = type
                this.amount = it.amountDecimal
                this.price = it.priceDecimal
            }
        }
        order
    }
}

val ordersRepository: OrderDaoImpl = OrderDaoImpl()