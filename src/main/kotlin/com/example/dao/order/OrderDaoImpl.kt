package com.example.dao.order

import com.example.service.DatabaseFactory.dbQuery
import com.example.models.OrderEntity
import com.example.models.OrderItemTable
import com.example.models.OrderTable
import org.jetbrains.exposed.sql.selectAll

class OrderDaoImpl: OrderDaoFacade {
    override suspend fun findAllOrders(): List<OrderEntity> = dbQuery {
        val orders = OrderTable.leftJoin(OrderItemTable).selectAll()
        OrderEntity.wrapRows(orders).toList()
    }

    override suspend fun findById(id: Int): OrderEntity? = dbQuery{
        OrderEntity.findById(id)
    }

    override suspend fun findTotalForOrder(id: Int): Int? = dbQuery {
        OrderEntity.findById(id)?.items?.sumOf { (it.amount * it.price) }?.toInt()
    }

//    private fun toOrder(row: ResultRow) = Order(
//        id = row[Orders.id].value,
//        userId = row[Orders.userId],
//        dateCreated = row[Orders.dateCreated],
//        details = row[Orders.details]
//    )
}

val ordersRepository: OrderDaoImpl = OrderDaoImpl()