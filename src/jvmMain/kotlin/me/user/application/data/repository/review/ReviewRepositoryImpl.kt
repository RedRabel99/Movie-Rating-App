package me.user.application.data.repository.review

import io.ktor.http.*
import me.user.application.data.service.review.ReviewService
import me.user.application.routes.review.params.CreateReviewParams
import me.user.application.routes.review.params.UpdateReviewParams
import BaseResponse

class ReviewRepositoryImpl(
    private val reviewService: ReviewService
) : ReviewRepository {
    override suspend fun getReviewList(): BaseResponse<Any> {
        return try{
            val result = reviewService.getReviewList()
            BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.OK)
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error getting review list")
        }
    }

    override suspend fun getReviewListForMovie(movieId: Int): BaseResponse<Any> {
        return try {
            val result = reviewService.getReviewListForMovie(movieId)
            BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.OK)
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error getting review list for movie")
        }
    }

    override suspend fun getReviewListForUser(userId: Int): BaseResponse<Any> {
        return try {
            val result = reviewService.getReviewListForUser(userId)
            BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.OK)
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error getting review list for user")
        }
    }

    override suspend fun getReview(id: Int): BaseResponse<Any> {
        return try {
            val result = reviewService.getReview(id)
            if (result != null) {
                BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.OK)
            } else {
                BaseResponse.ErrorResponse(message = "Review not found", statusCode = HttpStatusCode.NotFound)
            }
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error getting review", statusCode = HttpStatusCode.InternalServerError)
        }
    }

    override suspend fun createReview(params: CreateReviewParams): BaseResponse<Any> {
        return try {
            val result = reviewService.createReview(params)
            reviewService.updateMovieRating(result.movieId, result.rating, 1)
            return BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.Created)
        }catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error creating review")
        }
    }

    override suspend fun updateReview(params: UpdateReviewParams): BaseResponse<Any> {
        return try {
            val review = reviewService.getReview(params.id)
                ?: return BaseResponse.ErrorResponse(message = "Review not found", statusCode = HttpStatusCode.NotFound)

            if (review.userId != params.userId) {
                return BaseResponse.ErrorResponse(message = "User not authorized to update review", statusCode = HttpStatusCode.Unauthorized)
            }

            val result = reviewService.updateReview(review.copy(
                review = params.review?: review.review,
                rating = params.rating?: review.rating
            ))
            if(review.rating != result.rating) {
                reviewService.updateMovieRating(review.movieId, (result.rating - review.rating), 0)
            }
            BaseResponse.SuccessResponse(data = result, statusCode = HttpStatusCode.OK)
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error updating review", statusCode = HttpStatusCode.InternalServerError)
        }
    }

    override suspend fun deleteReview(id: Int, userId: Int): BaseResponse<Any> {
        return try {
            val review = reviewService.getReview(id)
                ?: return BaseResponse.ErrorResponse(message = "Review not found", statusCode = HttpStatusCode.NotFound)

            if (review.userId != userId) {
                return BaseResponse.ErrorResponse(message = "User not authorized to update review", statusCode = HttpStatusCode.Unauthorized)
            }
            val result = reviewService.deleteReview(id)
            if (result) {
                BaseResponse.SuccessResponse(statusCode = HttpStatusCode.NoContent)
            } else {
                BaseResponse.ErrorResponse(message = "Review not found", statusCode = HttpStatusCode.NotFound)
            }
        }
        catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message ?: "Error deleting review", statusCode = HttpStatusCode.InternalServerError)
        }
    }
}