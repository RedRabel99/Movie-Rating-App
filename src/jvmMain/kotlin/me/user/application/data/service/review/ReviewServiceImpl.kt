package me.user.application.data.service.review

import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.ReviewTable
import me.user.application.routes.review.params.CreateReviewParams
import models.Review
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement

class ReviewServiceImpl : ReviewService {
    override suspend fun getReviewList(): List<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewListForMovie(movieId: Int): List<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewListForUser(userId: Int): List<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getReview(id: Int): Review? {
        TODO("Not yet implemented")
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
        return rowToReview(statement?.resultedValues?.get(0)) ?: throw Exception("Review not created")
    }

    private fun rowToReview(row: ResultRow?): Review? {
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
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}