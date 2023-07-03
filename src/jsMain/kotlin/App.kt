
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState

private val scope = MainScope()

val App = FC<Props> {
    var movieList by useState(emptyList<Movie>())
    var sortKey by useState("title") // Default sorting key
    var sortAscending by useState(true) // Default sorting order

    useEffectOnce {
        scope.launch {
            movieList = getMovieList()
            console.log(movieList)
        }
    }

    fun sortMovieList() {
        movieList = when (sortKey) {
            "title" -> if (sortAscending) movieList.sortedBy { it.title } else movieList.sortedByDescending { it.title }
            "score" -> if (sortAscending) movieList.sortedBy { it.score } else movieList.sortedByDescending { it.score }
            "reviewCount" -> if (sortAscending) movieList.sortedBy { it.reviewCount } else movieList.sortedByDescending { it.reviewCount }
            else -> movieList
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }

    // Sorting dropdown list
    div {
        select {
            value = sortKey
            onChange = { event ->
                sortKey = (event.target).value
                sortMovieList()
            }
            option { +"title" }
            option { +"score" }
            option { +"reviewCount" }
        }

        // Sorting order buttons
        button {
            onClick = {
                sortAscending = !sortAscending
                sortMovieList()
            }
            if (sortAscending) {
                +"Sort Ascending"
            } else {
                +"Sort Descending"
            }
        }
    }

    ul {
        movieList.forEach { item ->
            li {
                a{href = "/movies/${item.id}"
                    +"${item.title} score: ${item.score} review count: ${item.reviewCount}"
                }
            }
        }
    }
}
