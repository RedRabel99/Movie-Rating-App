package me.user.application.routes.movie

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.data.repository.movie.MovieRepository
import models.Genre
import models.Movie

fun Application.movieRoutes(repository: MovieRepository){
    routing{
        route(Movie.path){
            get {
                if (call.request.queryParameters["genre"] != null){
                    val genre = call.request.queryParameters["genre"]!!
                    val result = repository.getMovieListByGenre(genre)
                    call.respond(status = result.statusCode, result)
                }
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

            get(Genre.path){
                val result = repository.getGenresList()
                call.respond(status = result.statusCode, result)
            }

            get("${Genre.path}/{id}"){
                val id = call.parameters["id"]?.toIntOrNull()
                if(id == null) {
                    call.respondText("Invalid id provided",status = HttpStatusCode.BadRequest)
                } else {
                    val result = repository.getGenreById(id)
                    call.respond(status = result.statusCode, result)
                }
            }
        }
    }
}