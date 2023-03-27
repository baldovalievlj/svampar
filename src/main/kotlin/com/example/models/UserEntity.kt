package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable: IntIdTable("user") {
    val username = varchar("username", 128).uniqueIndex()
    val role = enumeration<Role>("role")
    val password = varchar("password", 128)
    val email = varchar("email", 128)
    val firstName = varchar("first_name", 30)
    val lastName = varchar("last_name", 30)
    val phoneNumber = varchar("phone_number", 30).nullable()
}
private const val DEFAULT_VARCHAR_COLUMN_LENGTH = 255

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)
    val username by UserTable.username
    val role by UserTable.role
    val password by UserTable.password
    val email by UserTable.email
    val firstName by UserTable.firstName
    val lastName by UserTable.lastName
    val phoneNumber by UserTable.phoneNumber
    val orders by OrderEntity referrersOn OrderTable.user
}