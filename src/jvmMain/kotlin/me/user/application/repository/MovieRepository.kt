package me.user.application.repository

import me.user.application.utils.BaseResponse

interface MovieRepository {
    suspend fun getMovieList(): BaseResponse<Any>
    suspend fun getMovieById(id: Int): BaseResponse<Any>
}