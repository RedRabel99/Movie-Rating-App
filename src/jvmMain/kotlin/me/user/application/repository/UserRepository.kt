package me.user.application.repository

import me.user.application.service.CreateLoginParams
import me.user.application.service.CreateUserParams
import me.user.application.utils.BaseResponse

interface UserRepository {
    suspend fun registerUser(params: CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(params: CreateLoginParams): BaseResponse<Any>
}