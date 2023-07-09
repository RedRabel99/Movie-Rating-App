package components

import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.PublicUser
import models.Review
import mui.material.Grid
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.router.useParams
import services.getReviewsByUser
import services.getUser
import utils.md
import utils.sm
import utils.xs

private val scope = MainScope()

val UserReviews = FC<Props>{
    val params = useParams()
    var id = params["id"]?.toInt() ?: 0
    var user: PublicUser? by useState(null)
    var isLoading by useState(true) // Loading state
    var reviews by useState(emptyList<Review>())



    fun refreshReviews(){
        scope.launch {
            if (id != 0){
                try {
                    reviews = getReviewsByUser(id)
                } finally {
                    isLoading = false // Update loading state after the request is completed
                }
            }
        }
    }
    useEffectOnce{
        scope.launch {
            if (id != 0){
                try {
                    user = getUser(id)
                    reviews = getReviewsByUser(id)
                } finally {
                    isLoading = false // Update loading state after the request is completed
                }
            }
        }
    }
    useEffect{
        val previousId = id
        id = params["id"]?.toInt() ?: 0
        if (id != previousId){
            refreshReviews()
        }

    }


    if (isLoading) {
        +"Loading..."
    }else if (user == null){
        Typography{
            variant = TypographyVariant.h3
            +"User not found"
        }
    }else{
        Paper {
            elevation = 5
            sx {
                padding = 2.rem
                marginTop = 2.rem
            }

            Grid {
                container = true
                spacing = responsive(2)
                Grid {
                    item = true
                    xs = 12
                    Typography {
                        variant = TypographyVariant.h3
                        +"${user?.username}'s reviews"
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
                            showMovieTitle = true
                        }
                    }
                }
            }
        }
    }

}