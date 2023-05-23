package me.user.application.data.service.movie


import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.GenresTable
import me.user.application.data.models.MovieGenresTable
import me.user.application.data.models.MovieTable
import models.Genre
import models.Movie
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class MoviesServiceImpl : MoviesService {
    override suspend fun getMoviesList(): List<Movie?> {
        return try {
            val result = dbQuery {
                MovieTable.selectAll()
                    .map { resultRow -> resultRowToMovie(resultRow)
                    .let { movie ->
                        MovieGenresTable.leftJoin(GenresTable)
                        .select{MovieGenresTable.movie eq movie!!.id}
                        .map{Genre(it[GenresTable.id], it[GenresTable.name])}
                        .let { genres -> movie!!.copy(genres = genres)}
                    }
                }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf<Movie>()
        }

    }

    override suspend fun getMovieById(id: Int): Movie? {
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
        return movie
    }

    override suspend fun getGenresList(): List<Genre?> {
        return try {
            val result = dbQuery {
                GenresTable.selectAll()
                    .map { resultRowToGenre(it) }
            }
            result
        } catch (e: Exception){
            println(e)
            return listOf<Genre>()
        }
    }

    override suspend fun getMoviesByGenre(genreName: String): List<Movie?> {
        return try{
            dbQuery {
                MovieTable.innerJoin(MovieGenresTable)
                    .innerJoin(GenresTable)
                    .select { GenresTable.name eq genreName }
                    .map { resultRowToMovie(it).let{ movie ->
                        MovieGenresTable.leftJoin(GenresTable)
                            .select{MovieGenresTable.movie eq movie!!.id}
                            .map{Genre(it[GenresTable.id], it[GenresTable.name])}
                            .let { genres -> movie!!.copy(genres = genres)}
                    } }

            }
        } catch (e: Exception){
            println(e)
            return listOf<Movie>()
        }
    }

    override suspend fun getGenreById(id: Int): Genre? {
        return dbQuery {
                GenresTable.select { GenresTable.id eq id }
                    .mapNotNull { resultRowToGenre(it) }
                    .singleOrNull()
            }
    }

    private fun resultRowToMovie(row: ResultRow):Movie?{
        return try {
            Movie(
                id = row[MovieTable.id],
                title = row[MovieTable.title],
                reviewCount = row[MovieTable.review_count],
                score = row[MovieTable.rating].toFloat(),
                releaseDate = row[MovieTable.release_date].toString(),
                //runtime = row[MovieTable.runtime],
                overview = row[MovieTable.overview],
                posterPath = row[MovieTable.poster_path]

            )
        }catch (e: Exception){
            null
        }
    }

    private fun resultRowToGenre(row: ResultRow):Genre?{
        return try {
            Genre(
                id = row[GenresTable.id],
                name = row[GenresTable.name]
            )
        }catch (e: Exception){
            null
        }
    }
}