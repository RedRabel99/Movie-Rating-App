package me.user.application.repository

import io.ktor.http.*
import me.user.application.service.MoviesService
import me.user.application.utils.BaseResponse

class MovieRepositoryImpl(
    private val moviesService: MoviesService
) : MovieRepository {
    override suspend fun getMovieList(): BaseResponse<Any> {
        return try {
            val movieList = moviesService.getMoviesList()
            BaseResponse.SuccessResponse(data = movieList)
        }catch (e: Exception){
            BaseResponse.ErrorResponse(message ="Something went wrong", statusCode = HttpStatusCode.InternalServerError)
        }

    }

    override suspend fun getMovieById(id: Int): BaseResponse<Any> {
        return try{
            val movie = moviesService.getMovieById(id)
            if (movie == null) BaseResponse.ErrorResponse(message = "Not found", statusCode = HttpStatusCode.NotFound)
            else BaseResponse.SuccessResponse(data = movie)
        } catch (e: Exception){
            BaseResponse.ErrorResponse(message ="Something went wrong\n${e.message}", statusCode = HttpStatusCode.InternalServerError)
        }
    }
}