package com.example.dao.user

import com.example.models.UserEntity
import com.example.models.requests.UserRequest

interface UserDaoFacade {
    suspend fun createUser(userTable: UserRequest): Boolean
    suspend fun findByUsername(username: String): UserEntity?
    suspend fun findAllUsers(): List<UserEntity>
    suspend fun findById(id: Int): UserEntity?
    suspend fun deleteById(id: Int): Boolean
}