package com.example.models.entities

import com.example.models.Configuration
import com.example.models.ConfigurationType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ConfigurationsTable : IntIdTable("configuration.configuration") {
    val key = varchar("key", 255).uniqueIndex()
    val value = text("value")
    val type = enumeration<ConfigurationType>("type")
}

class ConfigurationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConfigurationEntity>(ConfigurationsTable)

    var key by ConfigurationsTable.key
    var value by ConfigurationsTable.value
    var type by ConfigurationsTable.type

    fun toConfiguration() = Configuration(key, value, type)
}