package com.example.models.response

import com.example.models.dto.TypeDTO

data class TypePaged(
    val types: List<TypeDTO>,
    override val totalCount: Int
): Pagination
