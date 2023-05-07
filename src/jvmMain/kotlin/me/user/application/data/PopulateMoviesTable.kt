package me.user.application.data

import kotlinx.serialization.json.*
import me.user.application.data.models.GenresTable
import me.user.application.data.models.MovieGenresTable
import me.user.application.data.models.MovieTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.math.BigDecimal
import java.time.LocalDate

fun populateTables(){
    if (GenresTable.selectAll().empty()) insertGenres(Json.parseToJsonElement(loadJsonFile("genres.json")))
    if (MovieTable.selectAll().empty()) insertMovies(Json.parseToJsonElement(loadJsonFile("movies.json")))
}

private fun insertMovies(jsonData: JsonElement) {
    jsonData.jsonObject["movies"]?.jsonArray?.forEach { jsonElement ->
        val title = jsonElement.jsonObject["title"]?.jsonPrimitive?.content
        val overview = jsonElement.jsonObject["overview"]?.jsonPrimitive?.content
        val posterPath = jsonElement.jsonObject["poster_path"]?.jsonPrimitive?.content
        val releaseDate = jsonElement.jsonObject["release_date"]?.jsonPrimitive?.content

        val movie = MovieTable.insert {
            it[MovieTable.title] = title ?: ""
            it[MovieTable.overview] = overview ?: ""
            it[MovieTable.poster_path] = posterPath ?: ""
            it[MovieTable.release_date] = LocalDate.parse(releaseDate ?: "2020-01-01").atStartOfDay()
            it[MovieTable.score] = BigDecimal(0.0)
            it[MovieTable.review_count] = 0

        }
        //insert 
        val movieId = movie.getOrNull(MovieTable.id)
            jsonElement.jsonObject["genres"]?.jsonArray?.forEach { genreId ->
            if (movieId != null) {
                MovieGenresTable.insert {
                    it[MovieGenresTable.movie] = movieId
                    it[MovieGenresTable.genre] = genreId.jsonPrimitive.int
                }
            }
        } }

}

private fun insertGenres(jsonData: JsonElement){
    jsonData.jsonObject["genres"]?.jsonArray?.forEach { jsonElement ->
        val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.int
        val name = jsonElement.jsonObject["name"]?.jsonPrimitive?.content
        if (id != null && name != null) {
            GenresTable.insert {
                it[GenresTable.id] = id
                it[GenresTable.name] = name
            }
        }

    }
}

private fun loadJsonFile(fileName: String): String {
    return object {}.javaClass.classLoader.getResource(fileName)?.readText() ?: ""
}

