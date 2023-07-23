package com.example.models.dto

import java.math.BigDecimal

data class OrderItemDTO(
    val id: Int,
    val comment: String?,
    val type: TypeDTO,
    val amount: BigDecimal,
    val price: BigDecimal
){
    constructor(
        id: Int,
        comment: String,
        type: TypeDTO,
        amount: String,
        price: String
    ) : this(
        id = id,
        comment = comment,
        type = type,
        amount = BigDecimal(amount),
        price = BigDecimal(price)
    )
}