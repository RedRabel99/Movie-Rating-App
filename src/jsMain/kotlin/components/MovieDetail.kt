
import components.MovieBox
import components.ReviewBox
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import models.Review
import mui.material.Grid
import mui.material.Paper
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.router.useParams
import react.useEffectOnce
import react.useState
import services.getMovie
import services.getReviews
import utils.md
import utils.sm
import utils.xs

private val scope = MainScope()

val MovieDetail: FC<Props> = FC<Props> {
    var movie: Movie? by useState(null)
    var reviews by useState(emptyList<Review>())
    var isLoading by useState(true) // Loading state
    val params = useParams()
    val id = params["id"]?.toInt() ?: 0

    useEffectOnce {
        scope.launch {
            try {
                movie = getMovie(id)
                reviews = getReviews(id)
            } finally {
                isLoading = false // Update loading state after the request is completed
            }
        }
    }

    div {
        if (isLoading) {
            +"Loading..." // Display loading message
        } else if (movie != null) {
            MovieBox{
                this.movie = movie as Movie
            }
            Paper{
                elevation = 5
                sx { padding = 2.rem
                    marginTop = 2.rem
                }
                Grid{
                    container = true
                    spacing = responsive(2)
                    reviews.forEach {review ->
                        Grid{
                            item = true
                            xs = 12
                            sm = 6
                            md = 4
                            ReviewBox{
                                this.review = review
                                isMoviePage = true
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
