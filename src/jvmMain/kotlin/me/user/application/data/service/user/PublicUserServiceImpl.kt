package me.user.application.data.service.user

import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.UserTable
import models.PublicUser
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class PublicUserServiceImpl : PublicUserService {
    override suspend fun getUser(id: Int): PublicUser? {
        val user = dbQuery{
            UserTable
                .select { UserTable.id.eq(id) }
                .mapNotNull { resultRowToPublicUser(it) }
                .singleOrNull()
        }
        return user
    }

    override suspend fun getUsers(): List<PublicUser> {
        return try {
            val result = dbQuery {
                UserTable.selectAll()
                    .map { resultRowToPublicUser(it) }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf()
        }
    }

    private fun resultRowToPublicUser(it: ResultRow): PublicUser {
        return PublicUser(
            id = it[UserTable.id],
            username = it[UserTable.username],
            createdAt = it[UserTable.createdAt].toString()
        )
    }
}