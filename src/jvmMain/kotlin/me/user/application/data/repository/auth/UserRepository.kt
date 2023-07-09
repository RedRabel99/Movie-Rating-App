package me.user.application.data.repository.auth

import params.CreateLoginParams
import params.CreateUserParams
import BaseResponse

interface UserRepository {
    suspend fun registerUser(params: CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(params: CreateLoginParams): BaseResponse<Any>
}