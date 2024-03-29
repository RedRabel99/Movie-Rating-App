package me.user.application.data.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object MovieTable: Table("movies") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val review_count = integer("review_count")
    val rating = float("rating")
    val release_date = datetime("release_date")
    val overview = text("overview")
    val poster_path = varchar("poster_path", 255)

    override val primaryKey = PrimaryKey(id)
}