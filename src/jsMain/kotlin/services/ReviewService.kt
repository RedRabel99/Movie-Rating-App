package services

import SyncResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import models.Review
import services.jsonClient.client

suspend fun getReviews(id: Int): List<Review> {
    return client.get("/movies/$id/reviews").body<SyncResponse<List<Review>>>().data?: emptyList()
}

suspend fun getReview(id: Int): Review? {
    return client.get("/reviews/$id").body<SyncResponse<Review>>().data
}