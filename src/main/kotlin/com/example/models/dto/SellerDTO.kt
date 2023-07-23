package com.example.models.dto

data class SellerDTO(
    val id: Int,
    val name: String,
    val socialSecurityNumber: String,
    val address: String?,
    val phoneNumber: String?,
    val email: String,
    val additionalInfo: String?
)