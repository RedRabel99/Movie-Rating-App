
import components.MovieBox
import components.ReviewBox
import components.ReviewCreationBox
import context.useUser
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import models.Review
import mui.material.Box
import mui.material.Grid
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import react.router.useParams
import services.getMovie
import services.getReviews
import utils.md
import utils.sm
import utils.xs

private val scope = MainScope()

val MovieDetail: FC<Props> = FC<Props> {
    var movie: Movie? by useState(null)
    var reviews by useState(emptyList<Review>())
    var userReview: Review? by useState(null)
    var isLoading by useState(true) // Loading state
    val params = useParams()
    val id = params["id"]?.toInt() ?: 0
    val userContext = useUser()


    fun findUserReview(){
        console.log("findUserReview ${userContext?.user?.id}")
        userReview = reviews.find { review -> review.userId == userContext?.user?.id }
        console.log(reviews)
        console.log("findUserReview ${userReview}")
    }
    useEffectOnce {
        scope.launch {
            try {
                movie = getMovie(id)
                reviews = getReviews(id)

            } finally {
                isLoading = false // Update
            }
        }
    }

    useEffect{
        findUserReview()
    }

    fun refreshReviews(){
        scope.launch {
            try {
                reviews = getReviews(id)
                movie = getMovie(id)

            } finally {
                isLoading = false // Update loading state after the request is completed
                findUserReview()
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
                    Grid{
                        item = true
                        xs = 12
                        if(userContext?.user != null){
                            if(userReview != null){
                                Box{
                                    Typography{
                                        variant = TypographyVariant.h4
                                        +"Your review"
                                    }
                                    ReviewBox{
                                        this.review = userReview as Review
                                        this.refreshReviews = ::refreshReviews
                                        showMovieTitle = false
                                    }
                                }

                            }else{
                                ReviewCreationBox{
                                    this.movieId = id
                                    this.user = userContext.user
                                    this.refreshReviews = ::refreshReviews
                                }
                            }
                        }else{
                            Typography{
                                variant = TypographyVariant.h4
                                +"Please login to add a review"
                            }
                        }


                    }
                    Grid{
                        item = true
                        xs = 12
                        Typography{
                            variant = TypographyVariant.h4
                            +"All reviews"
                        }
                    }
                    reviews.forEach {review ->
                        Grid{
                            item = true
                            xs = 12
                            sm = 6
                            md = 4
                            ReviewBox{
                                this.review = review
                                this.refreshReviews = ::refreshReviews
                                showMovieTitle = false
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
