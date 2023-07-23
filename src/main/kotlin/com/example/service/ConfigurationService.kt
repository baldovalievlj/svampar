package com.example.service

import com.example.dao.configuration.ConfigurationRepository
import com.example.models.Configuration
import com.example.service.ConfigurationKeys.EMAIL_API
import com.example.service.ConfigurationKeys.EMAIL_FROM
import com.example.service.ConfigurationKeys.EMAIL_FROM_NAME
import com.example.service.ConfigurationKeys.EMAIL_SUBJECT
import com.example.service.ConfigurationKeys.EMAIL_TEMPLATE

class ConfigurationService(private val repository: ConfigurationRepository) {

    fun getString(config: Configuration):String = repository.getConfiguration(config)?.value ?: config.value

    fun getNumber(config: Configuration):Int = repository.getConfiguration(config)?.value?.toInt() ?: config.value.toInt()

    fun getAll(): List<Configuration> {
        val configsFromDB = repository.getAllConfigurations().associateBy { it.key }
        val defaultConfigs = listOf(
            EMAIL_TEMPLATE,
            EMAIL_SUBJECT,
            EMAIL_FROM,
            EMAIL_FROM_NAME,
            EMAIL_API
        )
        return defaultConfigs.map { defaultConfig ->
            configsFromDB[defaultConfig.key] ?: defaultConfig
        }
    }
    val emailTemplate: String
        get() = getString(EMAIL_TEMPLATE)

    val emailSubject: String
        get() = getString(EMAIL_SUBJECT)

    val emailFrom: String
        get() = getString(EMAIL_FROM)

    val emailFromName: String
        get() = getString(EMAIL_FROM_NAME)

    val emailApi: String
        get() = getString(EMAIL_API)
}