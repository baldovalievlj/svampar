package com.example.models.entities

import com.example.models.dto.OrderDTO
import com.example.models.dto.OrderItemDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object OrderTable : IntIdTable("order.order") {
    val user = reference("user_id", UserTable)
    val seller = reference("seller_id", SellerTable)
    val dateCreated = datetime("date_created")
    val details = text("details").nullable()
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)

    var user by UserEntity referencedOn OrderTable.user
    var seller by SellerEntity referencedOn OrderTable.seller
    var dateCreated by OrderTable.dateCreated
    var details by OrderTable.details
    val items by OrderItemEntity referrersOn OrderItemTable.order

    fun toOrderDTO(): OrderDTO {
        val orderItems: List<OrderItemDTO> = items.map { it.toOrderItemDTO() }

        return OrderDTO(
            id = id.value,
            user = user.toUserDTO(),
            seller = seller.toSellerDTO(),
            dateCreated = dateCreated.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME),
            details = details,
            items = orderItems,
            totalAmount = orderItems.fold(BigDecimal.ZERO) { sum, item -> sum.add(item.amount) },
            totalPrice = orderItems.fold(BigDecimal.ZERO) { sum, item -> sum.add(item.price * item.amount) },
        )
    }
}
