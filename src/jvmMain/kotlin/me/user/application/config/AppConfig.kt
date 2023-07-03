package me.user.application.config

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.user.application.data.DatabaseFactory
import me.user.application.routes.auth.authRoutes
import me.user.application.routes.movie.movieRoutes
import me.user.application.routes.review.reviewRoutes
import me.user.application.utils.RepositoryProvider

fun configureDatabase() {
    DatabaseFactory.init()
}

fun Application.configureContentNegotiation() {
    install(ContentNegotiation){
        jackson()
    }
}

fun Application.frontendRoutes() {
    routing {
        route("/app") {
            get {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
        }

        static("/") {
            resources("")

        }
    }
}

fun Application.configureRouting(){
    frontendRoutes()
    movieRoutes(RepositoryProvider.provideMovieRepository())
    authRoutes(RepositoryProvider.provideAuthRepository())
    reviewRoutes(RepositoryProvider.provideReviewRepository())
}