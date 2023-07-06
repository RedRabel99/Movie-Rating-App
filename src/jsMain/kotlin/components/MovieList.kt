package components

import csstype.rem
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Genre
import models.Movie
import mui.material.Paper
import mui.system.sx
import org.w3c.dom.url.URLSearchParams
import react.FC
import react.Props
import react.useEffectOnce
import react.useState
import services.getGenresList
import services.getMovieList
import services.getMovieListByGenre

private val scope = MainScope()

val MovieList = FC<Props> {
    var genreList by useState(emptyList<Genre>())
    val genreParam = URLSearchParams(window.location.href.split("?").getOrNull(1)).get("genre") //Workaround for getting query params cause location.search doesnt work correctly
    var genreId by useState(genreParam?:"")
    var movieList by useState(emptyList<Movie>())
    var isLoading by useState(true) // Loading state
    useEffectOnce {
        scope.launch {
            try {
                console.log("genreId: $genreParam")
                genreList = getGenresList()
                movieList = if (genreParam != null && genreParam != "") {
                    getMovieListByGenre(genreParam as String)
                }else{
                    getMovieList()
                }
            } finally {
                isLoading = false // Update loading state after the request is completed
            }
        }
    }
    fun updateMovieList(genreName: String){
        scope.launch {
            try {
                movieList = if (genreName != "") {
                    getMovieListByGenre(genreName)
                }else{
                    getMovieList()
                }
            } finally {
                console.log(movieList)
                isLoading = false // Update loading state after the request is completed
            }
        }
    }

    fun onGenreSelect(genreName: String){
        genreId = genreName
        updateMovieList(genreName)
    }


    Paper{
        elevation = 6
        sx { padding = 2.rem }
        GenreSelector{
            genres = genreList
            selectedGenre = genreId
            this.onGenreSelect = {genre -> onGenreSelect(genre)}
        }
        MovieTable{
            this.movieList = movieList
        }
    }
}