package me.user.application.utils

import me.user.application.data.repository.auth.UserRepository
import me.user.application.data.repository.auth.UserRepositoryImpl
import me.user.application.data.repository.movie.MovieRepository
import me.user.application.data.repository.movie.MovieRepositoryImpl
import me.user.application.data.repository.review.ReviewRepository
import me.user.application.data.repository.review.ReviewRepositoryImpl
import me.user.application.data.service.auth.UserServiceImpl
import me.user.application.data.service.movie.MoviesServiceImpl
import me.user.application.data.service.review.ReviewServiceImpl

object RepositoryProvider {
    fun provideMovieRepository(): MovieRepository = MovieRepositoryImpl(MoviesServiceImpl())
    fun provideAuthRepository(): UserRepository = UserRepositoryImpl(UserServiceImpl())
    fun provideReviewRepository(): ReviewRepository = ReviewRepositoryImpl(ReviewServiceImpl())
}