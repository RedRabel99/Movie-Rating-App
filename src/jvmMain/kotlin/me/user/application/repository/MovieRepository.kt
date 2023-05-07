package me.user.application.repository

import me.user.application.utils.BaseResponse

interface MovieRepository {
    suspend fun getMovieList(): BaseResponse<Any>
    suspend fun getMovieById(id: Int): BaseResponse<Any>
    suspend fun getGenresList(): BaseResponse<Any>
    suspend fun getMovieListByGenre(genreName: String): BaseResponse<Any>
    suspend fun getGenreById(id: Int): BaseResponse<Any>
}