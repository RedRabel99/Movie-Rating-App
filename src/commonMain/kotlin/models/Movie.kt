package models

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val reviewCount: Int,
    val score: Float,
    val releaseDate: String,
    val runtime: Int,
    val overview: String,
    val posterPath: String,
    val genres: List<Genre> = emptyList()
    ) {

    companion object{
        const val path = "/movies"
    }
}