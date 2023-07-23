package com.example.models.entities

import com.example.models.dto.TypeDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TypeTable: IntIdTable("order.type") {
    val name = varchar("name", 30)
    val description = varchar("description", 30).nullable()
    val deleted = bool("deleted")
}

class TypeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TypeEntity>(TypeTable)
    var name by TypeTable.name
    var description by TypeTable.description
    var deleted by TypeTable.deleted

    fun toTypeDTO() = TypeDTO(
        id = id.value,
        name = name,
        description = description
    )
}

