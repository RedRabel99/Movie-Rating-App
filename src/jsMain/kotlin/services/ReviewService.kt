package services

import SyncResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import models.Review
import params.CreateReviewParams
import params.UpdateReviewParams
import services.jsonClient.client

suspend fun getReviews(id: Int): List<Review> {
    return client.get("/movies/$id/reviews").body<SyncResponse<List<Review>>>().data?: emptyList()
}

suspend fun getReview(id: Int): Review? {
    return client.get("/reviews/$id").body<SyncResponse<Review>>().data
}

suspend fun getReviewsByUser(userId: Int): List<Review> {
    return client.get("/users/$userId/reviews").body<SyncResponse<List<Review>>>().data?: emptyList()
}

suspend fun createReview(movieId: Int, rating: Int, review: String, token: String): Boolean {
    val response = client.post("/reviews") {
        contentType(io.ktor.http.ContentType.Application.Json)
        setBody(
                Json.encodeToJsonElement(CreateReviewParams.serializer(),
                CreateReviewParams(
                    rating = rating,
                    review= review,
                    movieId = movieId)))
        bearerAuth(token)
    }.body<SyncResponse<Review>>()
    return response.statusCode.description == "Created" && response.data != null
}

suspend fun editReview(id: Int, rating: Int, review: String, token: String): Boolean {
    val response = client.patch("/reviews/$id") {
        contentType(ContentType.Application.Json)
        setBody(
                Json.encodeToJsonElement(UpdateReviewParams.serializer(),
                UpdateReviewParams(
                    rating = rating,
                    review= review)))
        bearerAuth(token)
    }.body<SyncResponse<Review>>()
    return response.statusCode.description == "OK"
}

suspend fun deleteReview(id: Int, token: String): Boolean {
    val response = client.delete("/reviews/$id") {
        contentType(ContentType.Application.Json)
        bearerAuth(token)
    }.body<SyncResponse<Review>>()
    return response.statusCode.description == "No Content"
}