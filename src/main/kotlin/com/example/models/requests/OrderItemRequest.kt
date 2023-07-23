package com.example.models.requests

import java.math.BigDecimal

data class OrderItemRequest (
    val type: Int,
    val amount: String,
    val price: String,
    val comment: String
) {
    val amountDecimal: BigDecimal
        get() = BigDecimal(amount)

    val priceDecimal: BigDecimal
        get() = BigDecimal(price)
}
