import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.router.useParams
import react.useEffectOnce
import react.useState
import services.getMovie

private val scope = MainScope()

val MovieDetail: FC<Props> = FC<Props> {
    var movieList: Movie? by useState(null)
    var isLoading by useState(true) // Loading state
    val params = useParams()
    val id = params["id"]?.toInt() ?: 0

    useEffectOnce {
        scope.launch {
            try {
                movieList = getMovie(id)
            } finally {
                isLoading = false // Update loading state after the request is completed
            }
        }
    }

    div {
        if (isLoading) {
            +"Loading..." // Display loading message
        } else if (movieList != null) {
            +"Movie Detail: ${movieList.toString()}" // Display movie details
        } else {
            +"No movie found." // Display message when movie data is not available
        }
    }
}
