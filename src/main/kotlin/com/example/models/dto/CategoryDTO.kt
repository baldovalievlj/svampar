package com.example.models.dto

import kotlinx.serialization.Serializable


@Serializable
data class CategoryDTO(
    val id: Long,
    val name: String
)