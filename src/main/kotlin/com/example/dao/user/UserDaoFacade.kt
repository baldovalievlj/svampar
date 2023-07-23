package com.example.dao.user

import com.example.models.entities.UserEntity
import com.example.models.requests.PasswordRequest
import com.example.models.requests.UserRequest
import com.example.models.response.UserPaged

interface UserDaoFacade {
    suspend fun findAll(): List<UserEntity>
    suspend fun findAllPaged(limit: Int, offset: Int): UserPaged
    suspend fun findByUsername(username: String): UserEntity?
    suspend fun findById(id: Int): UserEntity?
    suspend fun create(user: UserRequest): Boolean
    suspend fun updateUser(id: Int, user: UserRequest): Boolean
    suspend fun updateUserPassword(id: Int, request: PasswordRequest): Boolean
    suspend fun deleteById(id: Int): Boolean
}