package me.user.application.routes.review

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.data.repository.review.ReviewRepository
import me.user.application.routes.review.params.CreateReviewParams
import me.user.application.routes.review.params.UpdateReviewParams
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
            get("/{id}"){
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null){
                    call.respondText("Bad request", status = HttpStatusCode.BadRequest)
                    return@get
                }
                val result = repository.getReview(id)
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

                patch("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null){
                        call.respondText("Bad request", status = HttpStatusCode.BadRequest)
                        return@patch
                    }
                    val user = call.authentication.principal<UserIdPrincipalForUser>()
                    if (user == null){
                        call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                        return@patch
                    }
                    val params: UpdateReviewParams = call.receive<UpdateReviewParams>()
                    params.userId = user.id
                    params.id = id
                    val result = repository.updateReview(params)
                    call.respond(status = result.statusCode, result)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null){
                        call.respondText("Bad request", status = HttpStatusCode.BadRequest)
                        return@delete
                    }
                    val user = call.authentication.principal<UserIdPrincipalForUser>()
                    if (user == null){
                        call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                        return@delete
                    }
                    val result = repository.deleteReview(id, user.id)
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