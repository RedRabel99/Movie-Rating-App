package components

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import react.FC
import react.Props
import react.useEffectOnce
import react.useState
import services.getMovieList

private val scope = MainScope()

val MovieList = FC<Props> {
    var movieList by useState(emptyList<Movie>())

    useEffectOnce {
        scope.launch {
            movieList = getMovieList()
        }
    }
    MovieTable{
        this.movieList = movieList
    }
}
