package me.user.application.service

import models.Movie

interface MoviesService {
    suspend fun getMoviesList(): List<Movie?>
    suspend fun getMovieById(id: Int): Movie?
    //suspend fun
}