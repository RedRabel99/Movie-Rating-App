package me.user.application.service


import models.Movie
import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.GenresTable
import me.user.application.data.models.MovieGenresTable
import me.user.application.data.models.MovieTable
import models.Genre
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class MoviesServiceImpl : MoviesService {
    override suspend fun getMoviesList(): List<Movie?> {
        return try {
            val result = dbQuery {
                MovieTable.selectAll().map { resultRow ->  resultRowToMovie(resultRow)}
            }
            result
        } catch (e: Exception){
            println()
            return listOf<Movie>()
        }

    }

    override suspend fun getMovieById(id: Int): Movie? {
//        val movie = dbQuery {
//                MovieTable.select{
//                    MovieTable.id.eq(id)
//                }.map {resultRowToMovie(it)}.singleOrNull()
//            }
        val movie = dbQuery {

            MovieTable
                .select { MovieTable.id.eq(id) }
                .mapNotNull { resultRowToMovie(it) }
                .singleOrNull()?.let { movie ->
                    MovieGenresTable.leftJoin(GenresTable)
                        .select{MovieGenresTable.movie eq id}
                        .map{Genre(it[GenresTable.id], it[GenresTable.name])}
                        .let { genres ->
                            movie.copy(genres = genres)
                        }
                }
        }
        println("-------------------------")
        println(movie)
        return movie
    }

    private fun resultRowToMovie(row: ResultRow):Movie?{
        return try {
            Movie(
                id = row[MovieTable.id],
                title = row[MovieTable.title],
                reviewCount = row[MovieTable.review_count],
                score = row[MovieTable.score].toFloat(),
                releaseDate = row[MovieTable.release_date].toString(),
                runtime = row[MovieTable.runtime],
                overview = row[MovieTable.overview],
                posterPath = row[MovieTable.poster_path]

            )
        }catch (e: Exception){
            null
        }
    }
}