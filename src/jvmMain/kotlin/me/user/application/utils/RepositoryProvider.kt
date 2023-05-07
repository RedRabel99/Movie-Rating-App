package me.user.application.utils

import me.user.application.data.repository.auth.UserRepository
import me.user.application.data.repository.auth.UserRepositoryImpl
import me.user.application.data.repository.movie.MovieRepository
import me.user.application.data.repository.movie.MovieRepositoryImpl
import me.user.application.data.service.auth.UserServiceImpl
import me.user.application.data.service.movie.MoviesServiceImpl

object RepositoryProvider {
    fun provideMovieRepository(): MovieRepository = MovieRepositoryImpl(MoviesServiceImpl())
    fun provideAuthRepository(): UserRepository = UserRepositoryImpl(UserServiceImpl())
}