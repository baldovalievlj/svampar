package com.example.models.entities

import com.example.models.dto.OrderItemDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object OrderItemTable: IntIdTable("order_item") {
    val order = reference("order_id", OrderTable)
    val comment = varchar("comment", 128).nullable()
    val type = reference("type_id", TypeTable)
    val amount = double("amount")
    val price = double("price")
}

class OrderItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItemEntity>(OrderItemTable)
    var order by OrderEntity referencedOn OrderItemTable.order
    var comment by OrderItemTable.comment
    var type by TypeEntity referencedOn OrderItemTable.type
    var amount by OrderItemTable.amount
    var price by OrderItemTable.price

    fun toOrderItemDTO() = OrderItemDTO(
        id = id.value,
        comment = comment,
        type = type.toTypeDTO(),
        amount = amount,
        price = price
    )
}
