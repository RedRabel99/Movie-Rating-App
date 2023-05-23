package models

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int,
    val movieId: Int,
    val userId: Int,
    val rating: Int,
    val review: String,
    val createdAt: String,
) {
    companion object{
        const val path = "/reviews"
    }
}