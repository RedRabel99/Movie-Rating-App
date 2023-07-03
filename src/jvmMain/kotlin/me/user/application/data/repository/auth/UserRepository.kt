package me.user.application.data.repository.auth

import me.user.application.routes.auth.params.CreateLoginParams
import me.user.application.routes.auth.params.CreateUserParams
import BaseResponse

interface UserRepository {
    suspend fun registerUser(params: CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(params: CreateLoginParams): BaseResponse<Any>
}