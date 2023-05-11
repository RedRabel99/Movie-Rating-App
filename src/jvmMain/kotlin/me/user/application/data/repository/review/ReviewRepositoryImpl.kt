package me.user.application.data.repository.review

import io.ktor.http.*
import me.user.application.data.service.review.ReviewService
import me.user.application.routes.review.params.CreateReviewParams
import me.user.application.utils.BaseResponse

class ReviewRepositoryImpl(
    private val reviewService: ReviewService
) : ReviewRepository {
    override suspend fun getReviewList(): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewListForMovie(movieId: Int): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewListForUser(userId: Int): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getReview(id: Int): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun createReview(params: CreateReviewParams): BaseResponse<Any> {
        return try {
            val result = reviewService.createReview(params)
            return BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.Created)
        }catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error creating review")
        }
    }

    override suspend fun updateReview(params: CreateReviewParams): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(id: Int): BaseResponse<Any> {
        TODO("Not yet implemented")
    }
}