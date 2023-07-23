package com.example.models.response

import com.example.models.dto.UserDTO

data class UserPaged(
    val users: List<UserDTO>,
    override val totalCount: Int
) : Pagination