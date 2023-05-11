package me.user.application.routes.review.params
import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewParams(
    val movieId: Int,
    var userId: Int,
    val rating: Int,
    val review: String
)