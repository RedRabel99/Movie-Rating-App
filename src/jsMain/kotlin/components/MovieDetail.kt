import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.router.useParams
import react.useEffectOnce
import react.useState
import services.getMovie

private val scope = MainScope()

val MovieDetail: FC<Props> = FC<Props> {
    var movie: Movie? by useState(null)
    var isLoading by useState(true) // Loading state
    val params = useParams()
    val id = params["id"]?.toInt() ?: 0

    useEffectOnce {
        scope.launch {
            try {
                movie = getMovie(id)
            } finally {
                isLoading = false // Update loading state after the request is completed
            }
        }
    }

    div {
        if (isLoading) {
            +"Loading..." // Display loading message
        } else if (movie != null) {
            div{
                h2{
                    +"Title: ${movie?.title}"
                }
                p{
                    img{
                        src="https://image.tmdb.org/t/p/w300_and_h450_bestv2${movie?.posterPath}"
                        alt="$movie?.title poster"
                    }

                }
                p{
                    +"Description: ${movie?.overview}"
                }
                p{
                    +"Rating: ${movie?.score}"
                }
                p{
                    +"Release Date: ${movie?.releaseDate}"
                }
                p{
                    +"Review Count: ${movie?.reviewCount}"
                }
                p{
                    +"Genres:"
                    ul{
                        movie?.genres?.forEach { genre ->
                            li{
                                +genre.name
                            }
                        }
                    }
                }
            }
        } else {
            +"No movie found." // Display message when movie data is not available
        }
    }
}
