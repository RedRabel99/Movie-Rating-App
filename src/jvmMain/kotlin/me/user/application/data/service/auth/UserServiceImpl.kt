package me.user.application.data.service.auth

import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.UserTable
import me.user.application.routes.auth.params.CreateLoginParams
import me.user.application.routes.auth.params.CreateUserParams
import me.user.application.security.hashPassword
import me.user.application.security.isPasswordCorrect
import models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserServiceImpl : UserService {
    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = UserTable.insert {
                it[username] = params.username
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserByUsername(username: String): User? {
        val user = dbQuery {
            UserTable.select {
                UserTable.username.eq(username)
            }.map{rowToUser(it)}.singleOrNull()
        }
        return user
    }

    override suspend fun findUserByEmail(email: String): User? {
        val user = dbQuery {
            UserTable.select {
                UserTable.email.eq(email)
            }.map{rowToUser(it)}.singleOrNull()
        }
        return user
    }

    override suspend fun loginUser(params: CreateLoginParams): User? {
        val userRow = dbQuery { UserTable.select{UserTable.email eq params.email}.firstOrNull() } ?: return null
        return if (isPasswordCorrect(params.password, userRow[UserTable.password])) rowToUser(userRow) else null
    }

    private fun rowToUser(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            id = row[UserTable.id],
            username = row[UserTable.username],
            email = row[UserTable.email],
            createdAt = row[UserTable.createdAt].toString()
        )
    }
}