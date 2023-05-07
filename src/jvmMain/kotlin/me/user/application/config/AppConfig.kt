package me.user.application.config

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import me.user.application.data.DatabaseFactory
import me.user.application.routes.auth.authRoutes
import me.user.application.routes.movie.movieRoutes
import me.user.application.utils.RepositoryProvider

fun configureDatabase() {
    DatabaseFactory.init()
}

fun Application.configureContentNegotiation() {
    install(ContentNegotiation){
        jackson()
    }
}

fun Application.configureRouting(){
    movieRoutes(RepositoryProvider.provideMovieRepository())
    authRoutes(RepositoryProvider.provideAuthRepository())
}