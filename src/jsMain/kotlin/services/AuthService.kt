package services

import SyncResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json
import models.User
import params.CreateLoginParams
import params.CreateUserParams
import services.jsonClient.client

suspend fun login(email: String, password: String): Boolean{
    val response = client.post("auth/login"){
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToJsonElement(CreateLoginParams.serializer(), CreateLoginParams(email, password)))
    }.body<SyncResponse<User>>()
    if (response.statusCode.description == "OK" && response.data != null){
        localStorage.setItem("user", Json.encodeToString(User.serializer(), response.data))
        return true
    }
    return false
}

suspend fun register(username: String, email: String, password: String): Boolean{
    val response = client.post("auth/register"){
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToJsonElement(CreateUserParams.serializer(), CreateUserParams(username, email, password)))
    }.body<SyncResponse<User>>()
    //localStorage.setItem("user", Json.encodeToString(User.serializer(), response.data))
    return response.statusCode.description == "Created" && response.data != null
}

fun logout(){
    localStorage.removeItem("user")
}

fun getCurrentUser(): User?{
    val user = localStorage.getItem("user")
    return if (user != null){
        Json.decodeFromString(User.serializer(), user)
    }else{
        null
    }
}