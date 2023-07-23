package com.example.dao.configuration

import com.example.models.entities.ConfigurationEntity
import com.example.models.Configuration
import com.example.models.entities.ConfigurationsTable
import com.example.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.transactions.transaction

class ConfigurationRepository {

    fun getAllConfigurations(): List<Configuration> = transaction {
        ConfigurationEntity.all().map(ConfigurationEntity::toConfiguration)
    }

    fun getConfiguration(configuration: Configuration): Configuration? = transaction {
        ConfigurationEntity.find { ConfigurationsTable.key eq configuration.key }.firstOrNull()?.toConfiguration()
    }

    suspend fun createOrUpdateConfiguration(configuration: Configuration): Configuration = transaction {
        val config = ConfigurationEntity.find { ConfigurationsTable.key eq configuration.key }.firstOrNull()
        if (config != null) {
            config.value = configuration.value
            config.toConfiguration()
        } else {
            ConfigurationEntity.new {
                this.key = configuration.key
                this.value = configuration.value
                this.type = configuration.type
            }.toConfiguration()
        }
    }
}