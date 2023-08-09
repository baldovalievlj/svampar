package com.example.models.requests

data class PasswordChangeRequest(
    val currentPassword: String,
    val password: String,
    val confirmPassword: String
)
