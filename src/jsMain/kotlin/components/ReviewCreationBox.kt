package components

import csstype.AlignContent
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.User
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.aria.ariaReadOnly
import react.dom.html.ReactHTML.b
import react.dom.onChange
import react.useState
import services.createReview
import utils.xs

external interface ReviewCreationBoxPros: Props {
    var movieId: Int
    var user: User?
    var refreshReviews: () -> Unit
}

private val scope = MainScope()

val ReviewCreationBox = FC<ReviewCreationBoxPros>{props ->
    var rating:Int? by useState(null)
    var review by useState("")
    var reviewWasCreated by useState(false)

    fun handleReviewCreation(){
        if (props.user != null && rating != null){
            var result = false
            scope.launch{
                try{
                     result = createReview(props.movieId, rating!!, review, props.user!!.authToken!!)
                }finally{
                    if (result){
                        reviewWasCreated = true
                        props.refreshReviews()
                    }
                }
            }
        }
    }

    Card{
        sx{
            margin = 1.rem
        }
        CardContent{
            Grid{
                container = true
                spacing = responsive(2)
                sx{
                    alignContent = AlignContent.center
                }
                Grid{item = true
                    xs = 12
                    Typography{
                        variant = TypographyVariant.h4
                        b{
                            +"Add your review"
                        }
                    }
                }
                Grid{
                    item = true
                    xs = 12
                    Rating{
                        sx{
                            marginTop = 1.rem
                        }
                        name = "rating"
                        value = rating
                        max = 10
                        onChange = {_, newValue ->
                            rating = newValue as Int?
                        }
                    }
                }
                Grid{
                    item = true
                    xs = 12
                    TextField{
                        label = ReactNode("Your review")
                        variant = FormControlVariant.outlined
                        multiline = true
                        ariaReadOnly = false
                        fullWidth = true
                        rows = 5
                        value = review
                        onChange = {event ->
                            val target = event.target
                            val value = target.asDynamic().value as String? ?:""
                            review = value
                        }
                    }
                }
                Grid{
                    item = true
                    xs = 12
                    Button{
                        variant = ButtonVariant.contained
                        onClick = {_ -> handleReviewCreation() }
                        +"Add review"
                    }
                }
            }
        }
    }
}
