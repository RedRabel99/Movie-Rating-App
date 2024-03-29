package me.user.application.data.repository.review

import BaseResponse
import params.CreateReviewParams
import params.UpdateReviewParams

interface ReviewRepository {
    suspend fun getReviewList(): BaseResponse<Any>
    suspend fun getReviewListForMovie(movieId: Int): BaseResponse<Any>
    suspend fun getReviewListForUser(userId: Int): BaseResponse<Any>
    suspend fun getReview(id: Int): BaseResponse<Any>
    suspend fun createReview(params: CreateReviewParams): BaseResponse<Any>
    suspend fun deleteReview(id: Int, userId: Int): BaseResponse<Any>
    suspend fun updateReview(params: UpdateReviewParams): BaseResponse<Any>
}