package me.user.application.data.service.auth

import me.user.application.routes.auth.params.CreateLoginParams
import me.user.application.routes.auth.params.CreateUserParams
import models.User


interface UserService {
    suspend fun registerUser(params: CreateUserParams): User?

    suspend fun findUserByUsername(username: String):User?
    suspend fun findUserByEmail(email: String):User?
    suspend fun loginUser(params: CreateLoginParams): User?
}