package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val username: String,
    val password: String
)