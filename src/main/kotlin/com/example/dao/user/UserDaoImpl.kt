package com.example.dao.user

import com.example.service.DatabaseFactory.dbQuery
import com.example.models.Role
import com.example.models.entities.UserEntity
import com.example.models.entities.UserTable
import com.example.models.requests.PasswordRequest
import com.example.models.requests.UserRequest
import com.example.models.response.UserPaged
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.mindrot.jbcrypt.BCrypt

class UserDaoImpl : UserDaoFacade {

    override suspend fun create(user: UserRequest): Boolean = dbQuery {
        val insertStatement = UserTable.insert {
            it[username] = user.username
            it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            it[role] = Role.valueOf(user.role)
            it[email] = user.email
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[phoneNumber] = user.phoneNumber
        }
        insertStatement.resultedValues?.isNotEmpty() ?: false
    }

    override suspend fun updateUser(id: Int, user: UserRequest): Boolean = dbQuery {
        val updateStatement = UserTable.update({ UserTable.id eq id }) {
            it[username] = user.username
            it[role] = Role.valueOf(user.role)
            it[email] = user.email
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[phoneNumber] = user.phoneNumber
        }
        updateStatement > 0
    }

    override suspend fun updateUserPassword(id: Int, request: PasswordRequest): Boolean = dbQuery {
        val updateStatement = UserTable.update({ UserTable.id eq id }) {
            it[password] = BCrypt.hashpw(request.password, BCrypt.gensalt())
        }
        updateStatement > 0
    }

    override suspend fun findByUsername(username: String): UserEntity? = dbQuery {
        UserEntity.find((UserTable.username eq username)).firstOrNull { !it.deleted }
    }

    override suspend fun findAll(): List<UserEntity> = dbQuery {
        UserEntity.all().filter { !it.deleted }.toList()
    }

    override suspend fun findAllPaged(limit: Int, offset: Int): UserPaged = dbQuery {
        val users = UserEntity.find { UserTable.deleted eq false }.orderBy(UserTable.id to SortOrder.ASC)
        UserPaged(
            users = users.limit(limit, offset = offset.toLong()).toList().map { it.toUserDTO() },
            totalCount = users.count().toInt()
        )
    }

    override suspend fun findById(id: Int): UserEntity? = dbQuery {
        UserEntity.findById(id)?.takeUnless { it.deleted }
    }

    override suspend fun deleteById(id: Int): Boolean = dbQuery {
        UserEntity.findById(id)?.deleted = true
        true
    }

//    private fun toUser(row: ResultRow) = User(
//        id = row[Users.id],
//        username = row[Users.username],
//        password = row[Users.password],
//        email = row[Users.email],
//        firstName = row[Users.firstName],
//        lastName = row[Users.lastName],
//        phoneNumber = row[Users.phoneNumber]
//    )

}

val usersRepository: UserDaoImpl = UserDaoImpl()
