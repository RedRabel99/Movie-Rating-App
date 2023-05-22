package me.user.application.data.service.review

import me.user.application.routes.review.params.CreateReviewParams
import models.Review

interface ReviewService {
    suspend fun getReviewList(): List<Review>
    suspend fun getReviewListForMovie(movieId: Int): List<Review>
    suspend fun getReviewListForUser(userId: Int): List<Review>
    suspend fun getReview(id: Int): Review?
    suspend fun createReview(params: CreateReviewParams): Review
    suspend fun updateReview(review: Review): Review
    suspend fun deleteReview(id: Int): Boolean
    suspend fun updateMovieScore(movieId: Int, score: Int, reviewCountChange: Int): Boolean
}