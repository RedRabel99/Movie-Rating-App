package me.user.application.data.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    override val primaryKey = PrimaryKey(id)
}