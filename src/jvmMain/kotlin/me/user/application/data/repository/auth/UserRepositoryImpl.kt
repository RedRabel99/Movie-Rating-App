package me.user.application.data.repository.auth

import BaseResponse
import io.ktor.http.*
import me.user.application.data.service.auth.UserService
import me.user.application.security.JwtConfig
import params.CreateLoginParams
import params.CreateUserParams

class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): BaseResponse<Any> {
        if(emailOrUsernameExists(params.username, params.email))
            return BaseResponse.ErrorResponse(message = "User with those credentials already exists.")

        val user = userService.registerUser(params)
        if (user != null) {
            val token = JwtConfig.instance.createAccessToken(user.id)
            user.authToken = token
            return BaseResponse.SuccessResponse(data = user, statusCode = HttpStatusCode.Created)
        }
        return  BaseResponse.ErrorResponse(message = "Something went wrong.")
    }

    override suspend fun loginUser(params: CreateLoginParams): BaseResponse<Any> {
        val user = userService.loginUser(CreateLoginParams(params.email, params.password))
        if (user == null) return BaseResponse.ErrorResponse("Invalid credentials", statusCode = HttpStatusCode.Unauthorized)
        val token = JwtConfig.instance.createAccessToken(user.id)
        user.authToken = token
        return BaseResponse.SuccessResponse(data = user, message = "Hello ${user.username}")
    }

    private suspend fun emailOrUsernameExists(username: String, email: String): Boolean{
        return userService.findUserByUsername(username) != null || userService.findUserByEmail(email) != null
    }
}