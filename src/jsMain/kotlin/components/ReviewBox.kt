package components

import csstype.Display
import csstype.px
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import models.PublicUser
import models.Review
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.useEffectOnce
import react.useState
import services.getMovie
import services.getUser

external interface ReviewBoxProps: Props {
    var review: Review
    var isMoviePage: Boolean
}
private val scope = MainScope()

val ReviewBox = FC<ReviewBoxProps>{props ->
    var user: PublicUser? by useState(null)
    var movie: Movie? by useState(null)
    var isLoading by useState(true)

    useEffectOnce {
        scope.launch {
            try {
                movie = getMovie(props.review.movieId)
                user = getUser(props.review.userId)
            } finally {
              isLoading = false // Update loading state after the request is completed
            }
        }
    }

    Box{
        if(isLoading){
            Skeleton {
                variant = SkeletonVariant.text
                width = 210
            }
            Skeleton {
                variant = SkeletonVariant.circular
                width = 40
                height = 40
            }
            Skeleton {
                variant = SkeletonVariant.rectangular
                width = 210
                height = 118
            }
        } else if(movie != null && user != null){
            Card{
                sx{
                    margin = 1.rem
                }
                CardContent{
                    if (!props.isMoviePage){
                        Typography{
                            +movie?.title!!
                        }
                    }
                    Box{
                        sx {
                            display = Display.flex
                        }
                        Avatar{
                            alt = "account icon"
                        }
                        Typography{
                            sx{
                                padding = 9.px
                            }
                            +user?.username!!
                        }
                    }
                    Rating{
                        name = "read-only"
                        value = props.review.rating.toDouble()
                        readOnly = true
                        max = 10
                    }
                    Typography{
                        +props.review.review
                    }
                }
            }
        } else {
            +"Error loading review"
        }
    }
}