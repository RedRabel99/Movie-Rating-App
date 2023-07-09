package me.user.application.data.repository.user

import BaseResponse

interface PublicUserRepository {
    suspend fun getUser(id: Int): BaseResponse<Any>
    suspend fun getUsers(): BaseResponse<Any>
}