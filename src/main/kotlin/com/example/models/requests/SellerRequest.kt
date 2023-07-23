package com.example.models.requests

data class SellerRequest(
    val name: String,
    val socialSecurityNumber: String,
    val address: String? = null,
    val phoneNumber: String? = null,
    val email: String,
    val additionalInfo: String? = null
)
