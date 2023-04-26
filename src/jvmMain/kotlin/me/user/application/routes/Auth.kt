package me.user.application.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.repository.UserRepository
import me.user.application.service.CreateLoginParams
import me.user.application.service.CreateUserParams
import models.User

fun Application.authRoutes(repository: UserRepository){
    routing {
        route(User.path){
            post("/register") {
                val params = call.receive<CreateUserParams>()
                val result = repository.registerUser(params)

                call.respond(status = result.statusCode,result)
            }
            post("login"){
                val params = call.receive<CreateLoginParams>()
                val result = repository.loginUser(params)

                call.respond(status = result.statusCode, result)
            }
        }

    }
}