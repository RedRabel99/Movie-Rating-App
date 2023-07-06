package me.user.application.data.service.user

import models.PublicUser

interface PublicUserService {
    suspend fun getUser(id: Int): PublicUser?
    suspend fun getUsers(): List<PublicUser>
}