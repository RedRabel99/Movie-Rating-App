package me.user.application.data.repository.user

import BaseResponse
import io.ktor.http.*
import me.user.application.data.service.user.PublicUserService

class PublicUserRepositoryImpl(
    private val userService: PublicUserService
) : PublicUserRepository {
    override suspend fun getUser(id: Int): BaseResponse<Any> {
        return try{
            val user = userService.getUser(id)
            if (user == null) BaseResponse.ErrorResponse(message = "Not found", statusCode = HttpStatusCode.NotFound)
            else BaseResponse.SuccessResponse(data = user)
        }catch (e: Exception){
            BaseResponse.ErrorResponse(message ="Something went wrong\n${e.message}", statusCode = HttpStatusCode.InternalServerError)
        }
    }

    override suspend fun getUsers(): BaseResponse<Any> {
        return try{
            val userList = userService.getUsers()
            BaseResponse.SuccessResponse(data = userList)
        }catch (e: Exception){
            BaseResponse.ErrorResponse(message ="Something went wrong", statusCode = HttpStatusCode.InternalServerError)
        }
    }
}