package com.example.models.response

import com.example.models.dto.SellerDTO

data class SellerPaged(
    val sellers: List<SellerDTO>,
    override val totalCount: Int
) : Pagination