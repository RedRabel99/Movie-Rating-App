package services

import SyncResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import models.PublicUser
import services.jsonClient.client

suspend fun getUser(id: Int): PublicUser? {
    //return client.get("/users/$id").body<SyncResponse<User>>().data
    return client.get("/users/$id").body<SyncResponse<PublicUser>>().data
}

suspend fun getUsers(): List<PublicUser> {
    return client.get("/users").body<SyncResponse<List<PublicUser>>>().data?: emptyList()
}