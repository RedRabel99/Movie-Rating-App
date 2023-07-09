package me.user.application.data.service.review

import models.Review
import params.CreateReviewParams

interface ReviewService {
    suspend fun getReviewList(): List<Review>
    suspend fun getReviewListForMovie(movieId: Int): List<Review>
    suspend fun getReviewListForUser(userId: Int): List<Review>
    suspend fun getReview(id: Int): Review?
    suspend fun createReview(params: CreateReviewParams): Review
    suspend fun updateReview(review: Review): Review
    suspend fun deleteReview(id: Int): Review?
    suspend fun updateMovieRating(movieId: Int, rating: Int, reviewCountChange: Int): Boolean
}