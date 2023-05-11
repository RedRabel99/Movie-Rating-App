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
import models.Review

fun Application.reviewRoutes(repository: ReviewRepository){
    routing {
        route(Review.path){
            authenticate {
                post {
                    val xd = call.authentication.principal<UserIdPrincipalForUser>()
                    if (xd == null){
                        call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                        return@post
                    }
                    var params: CreateReviewParams = call.receive<CreateReviewParams>()
                    params.userId = xd.id
                    val result = repository.createReview(params)
                    call.respond(status = result.statusCode, result)
                }
            }

        }
    }
}