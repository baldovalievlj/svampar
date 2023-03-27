package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object OrderTable: IntIdTable("order") {
    val user = reference("user_id", UserTable)
    val dateCreated = datetime("date_created")
    val details = varchar("details", 500)
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)
    var user by UserEntity referencedOn OrderTable.user
    var dateCreated by OrderTable.dateCreated
    var details by OrderTable.details
    val items by OrderItemEntity referrersOn OrderItemTable.order
}
