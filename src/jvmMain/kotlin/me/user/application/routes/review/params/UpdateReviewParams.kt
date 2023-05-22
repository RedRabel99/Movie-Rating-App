package me.user.application.routes.review.params

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReviewParams(
    var id: Int,
    val score: Int?,
    val review: String?,
    var userId: Int,
)