package me.user.application.data.models

import org.jetbrains.exposed.sql.Table

object MovieGenresTable : Table(){
    val movie = reference("movie", MovieTable.id)
    val genre = reference("genre", GenresTable.id)
    override val primaryKey = PrimaryKey(movie, genre)
}