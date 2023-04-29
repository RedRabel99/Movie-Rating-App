package me.user.application.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.repository.MovieRepository
import models.Movie

fun Application.movieRoutes(repository: MovieRepository){
    routing{
        route(Movie.path){
            get {
                val result = repository.getMovieList()
                call.respond(status = result.statusCode, result)
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if(id == null) {
                    call.respondText("Invalid id provided",status = HttpStatusCode.BadRequest)
                } else {
                    val result = repository.getMovieById(id)
                    call.respond(status = result.statusCode, result)
                }
            }
            get("/genre") {
                call.respondText("siema")
            }
        }
    }
}