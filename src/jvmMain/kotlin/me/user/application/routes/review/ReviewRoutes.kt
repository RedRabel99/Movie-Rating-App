package me.user.application.routes.review

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.data.repository.review.ReviewRepository
import me.user.application.routes.review.params.CreateReviewParams
import me.user.application.security.UserIdPrincipalForUser
import models.Movie
import models.Review

fun Application.reviewRoutes(repository: ReviewRepository){
    routing {
        route(Review.path){
            get {
                val result = repository.getReviewList()
                call.respond(status = result.statusCode, result)
            }
            authenticate {
                post {
                    val xd = call.authentication.principal<UserIdPrincipalForUser>()
                    if (xd == null){
                        call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val params: CreateReviewParams = call.receive<CreateReviewParams>()
                    params.userId = xd.id
                    val result = repository.createReview(params)
                    call.respond(status = result.statusCode, result)
                }
            }
        }
        route(Movie.path){
            get("/{movieId}/${Review.path}"){
                val movieId = call.parameters["movieId"]?.toIntOrNull()
                if (movieId == null){
                    call.respondText("Bad request", status = HttpStatusCode.BadRequest)
                    return@get
                }
                val result = repository.getReviewListForMovie(movieId)
                call.respond(status = result.statusCode, result)
            }
        }
        route("/user"){
            get("/{userId}/${Review.path}"){
                val userId = call.parameters["userId"]?.toIntOrNull()
                if (userId == null){
                    call.respondText("Bad request", status = HttpStatusCode.BadRequest)
                    return@get
                }
                val result = repository.getReviewListForUser(userId)
                call.respond(status = result.statusCode, result)
            }
        }
    }
}