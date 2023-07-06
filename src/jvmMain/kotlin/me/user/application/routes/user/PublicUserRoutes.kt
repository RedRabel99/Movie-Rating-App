package me.user.application.routes.user

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.data.repository.user.PublicUserRepository

fun Application.publicUserRoutes(repository: PublicUserRepository){
    routing {
        route("/users"){
            get {
                val result = repository.getUsers()
                call.respond(status = result.statusCode, result)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if(id == null) {
                    call.respondText("Invalid id provided")
                } else {
                    val result = repository.getUser(id)
                    call.respond(status = result.statusCode, result)
                }
            }
        }
    }
}