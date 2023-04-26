package me.user.application

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import me.user.application.data.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import me.user.application.repository.UserRepository
import me.user.application.repository.UserRepositoryImpl
import me.user.application.routes.authRoutes
import me.user.application.security.configureSecurity
import me.user.application.service.UserService
import me.user.application.service.UserServiceImpl


fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/MovieRatingApp.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        DatabaseFactory.init()
        install(ContentNegotiation){
            jackson()
        }
        configureSecurity()
        install(CORS) {
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        val service: UserService = UserServiceImpl()
        val repository: UserRepository = UserRepositoryImpl(service)

        authRoutes(repository)
        routing {
            authenticate {
                route("xd"){
                    get {
                        call.respondText("siema")
                    }
                }
            }
        }

    }.start(wait = true)
}