package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object OrderItemTable: IntIdTable("order_item") {
    val order = reference("order_id", OrderTable)
    val item = varchar("item", 128).nullable()
    val category = reference("category_id", CategoryTable)
    val amount = integer("amount")
    val price = double("price")
}

class OrderItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItemEntity>(OrderItemTable)
    val order by OrderEntity referencedOn OrderItemTable.order
    val item by OrderItemTable.item
    val category by CategoryEntity referencedOn OrderItemTable.category
    val amount by OrderItemTable.amount
    val price by OrderItemTable.price
}
