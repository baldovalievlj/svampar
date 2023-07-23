package com.example.models.requests


data class PasswordRequest(
    val password: String,
    val confirmPassword: String
)