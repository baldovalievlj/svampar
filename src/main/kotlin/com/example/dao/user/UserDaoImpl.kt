package com.example.dao.user

import com.example.service.DatabaseFactory.dbQuery
import com.example.models.Role
import com.example.models.UserEntity
import com.example.models.UserTable
import com.example.models.requests.UserRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.mindrot.jbcrypt.BCrypt

class UserDaoImpl : UserDaoFacade {

    override suspend fun createUser(user: UserRequest): Boolean = dbQuery {
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

    override suspend fun findByUsername(username: String): UserEntity? = dbQuery {
        UserEntity.find((UserTable.username eq username)).firstOrNull()
    }

    override suspend fun findAllUsers(): List<UserEntity> = dbQuery {
        UserEntity.all().toList()
    }

    override suspend fun findById(id: Int): UserEntity? = dbQuery {
        UserEntity.findById(id)
    }

    override suspend fun deleteById(id: Int): Boolean = dbQuery {
        UserTable.deleteWhere { UserTable.id eq id } > 0
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
