package me.user.application.data.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ReviewTable: Table("reviews"){
    val id = integer("id").autoIncrement()
    val movieId = integer("movie_id").references(MovieTable.id)
    val userId = integer("user_id").references(UserTable.id)
    val rating = integer("rating")
    val review = varchar("review", 500)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}