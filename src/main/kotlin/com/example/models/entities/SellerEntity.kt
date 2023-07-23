package com.example.models.entities

import com.example.models.dto.SellerDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object SellerTable: IntIdTable("seller") {
    val name = varchar("name", 30)
    val socialSecurityNumber = varchar("social_security_number",30).uniqueIndex()
    val address = varchar("address", 100).nullable()
    val phoneNumber = varchar("phone_number", 15).nullable()
    val email = varchar("email", 30)
    val additionalInfo = varchar("additional_info",300).nullable()
    val deleted = bool("deleted")
}

class SellerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SellerEntity>(SellerTable)
    var name by SellerTable.name
    var socialSecurityNumber by SellerTable.socialSecurityNumber
    var address by SellerTable.address
    var phoneNumber by SellerTable.phoneNumber
    var email by SellerTable.email
    var additionalInfo by SellerTable.additionalInfo
    var deleted by SellerTable.deleted
    fun toSellerDTO() = SellerDTO(
        id = id.value,
        name = name,
        socialSecurityNumber = socialSecurityNumber,
        address = address,
        phoneNumber = phoneNumber,
        email = email,
        additionalInfo = additionalInfo
    )
}
