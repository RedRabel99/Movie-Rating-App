package services

import SyncResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import models.Genre
import models.Movie
import services.jsonClient.client

suspend fun getMovieList(): List<Movie> {
    return client.get("/movies").body<SyncResponse<List<Movie>>>().data?: emptyList()
}

suspend fun getMovie(id: Int): Movie? {
    //return client.get("/movies/$id").body<SyncResponse<Movie>>().data
    return client.get("/movies/$id").body<SyncResponse<Movie>>().data
}

suspend fun getGenresList(): List<Genre>{
    return client.get("/movies/genres").body<SyncResponse<List<Genre>>>().data?: emptyList()
}

suspend fun getMovieListByGenre(genre: String): List<Movie>{
    console.log("/movies?genre=${genre}")
    return client.get("/movies?genre=${genre}").body<SyncResponse<List<Movie>>>().data?: emptyList()
}