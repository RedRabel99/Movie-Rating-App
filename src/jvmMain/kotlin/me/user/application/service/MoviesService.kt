package me.user.application.service

import models.Genre
import models.Movie

interface MoviesService {
    suspend fun getMoviesList(): List<Movie?>
    suspend fun getMovieById(id: Int): Movie?
    suspend fun getGenresList(): List<Genre?>
    suspend fun getMoviesByGenre(genreName: String): List<Movie?>
    suspend fun getGenreById(id: Int): Genre?
}