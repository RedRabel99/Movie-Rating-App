package me.user.application.data.service.review

import io.ktor.server.plugins.*
import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.MovieTable
import me.user.application.data.models.ReviewTable
import me.user.application.routes.review.params.CreateReviewParams
import models.Review
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class ReviewServiceImpl : ReviewService {
    override suspend fun getReviewList(): List<Review> {
        return try {
            val result = dbQuery {
                ReviewTable.selectAll()
                    .mapNotNull { resultRowToReview(it) }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf<Review>()
        }
    }

    override suspend fun getReviewListForMovie(movieId: Int): List<Review> {
        return try {
            val result = dbQuery {
                ReviewTable.select { ReviewTable.movieId.eq(movieId) }
                    .mapNotNull { resultRowToReview(it) }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf<Review>()
        }
    }

    override suspend fun getReviewListForUser(userId: Int): List<Review> {
        return try {
            val result = dbQuery {
                ReviewTable.select { ReviewTable.userId.eq(userId) }
                    .mapNotNull { resultRowToReview(it) }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf<Review>()
        }
    }

    override suspend fun getReview(id: Int): Review? {
        return try {
            val result = dbQuery {
                ReviewTable.select { ReviewTable.id.eq(id) }
                    .mapNotNull { resultRowToReview(it) }
                    .singleOrNull()
            }
            result
        } catch (e: Exception){
            println(e)
            return null
        }
    }

    override suspend fun createReview(params: CreateReviewParams): Review {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = ReviewTable.insert {
                it[movieId] = params.movieId
                it[userId] = params.userId
                it[rating] = params.rating
                it[review] = params.review
            }
        }

        return resultRowToReview(statement?.resultedValues?.get(0)) ?: throw Exception("Review not created")
    }

    override suspend fun updateMovieRating(movieId: Int, rating: Int, reviewCountChange: Int): Boolean {
        try {
            dbQuery {
                MovieTable.select { MovieTable.id eq movieId }.singleOrNull()?.let { movie ->
                    val newReviewCount = movie[MovieTable.review_count] + reviewCountChange
                    val newRating = if(newReviewCount == 0) {0.0f}
                    else{
                        (movie[MovieTable.rating] * movie[MovieTable.review_count] + rating) / newReviewCount
                    }
                    MovieTable.update({ MovieTable.id eq movieId }) {
                        it[MovieTable.rating] = newRating
                        it[MovieTable.review_count] = newReviewCount
                    }
                }
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun resultRowToReview(row: ResultRow?): Review? {
        return if (row == null) null
        else Review(
            id = row[ReviewTable.id],
            movieId = row[ReviewTable.movieId],
            userId = row[ReviewTable.userId],
            rating = row[ReviewTable.rating],
            review = row[ReviewTable.review],
            createdAt = row[ReviewTable.createdAt].toString()
        )
    }

    override suspend fun updateReview(review: Review): Review {
        dbQuery {
            ReviewTable.update({ ReviewTable.id eq review.id }) {
                it[ReviewTable.rating] = review.rating
                it[ReviewTable.review] = review.review
            }
        }
        return getReview(review.id) ?: throw NotFoundException("Review not found")
    }

    override suspend fun deleteReview(id: Int): Boolean {
        val rating = getReview(id) ?: throw NotFoundException("Review not found")

        return if(dbQuery { ReviewTable.deleteWhere { ReviewTable.id eq id }} > 0){ // deleteWhere returns number of rows deleted
            updateMovieRating(rating.movieId, (-1 * rating.rating), -1)
            true
        } else {
            false
        }
    }
}
