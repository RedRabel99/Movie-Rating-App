package components

import context.useUser
import csstype.Display
import csstype.None
import csstype.px
import csstype.rem
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import models.Movie
import models.PublicUser
import models.Review
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.aria.ariaReadOnly
import react.dom.onChange
import react.router.dom.NavLink
import services.deleteReview
import services.editReview
import services.getMovie
import services.getUser

external interface ReviewBoxProps: Props {
    var review: Review
    var refreshReviews: () -> Unit
    var showMovieTitle: Boolean
}
private val scope = MainScope()

val ReviewBox = FC<ReviewBoxProps>{props ->
    var user: PublicUser? by useState(null)
    var movie: Movie? by useState(null)
    var isLoading by useState(true)
    var deletionDialogIsOpen by useState(false)
    var editDialogIsOpen by useState(false)
    var newRating by useState(props.review.rating)
    var newReview by useState(props.review.review)
    val userContext = useUser()

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
    fun delete(){
        if (userContext?.user?.id == props.review.userId){
            scope.launch {
                try {
                    deleteReview(props.review.id, userContext.user!!.authToken!!)

                }finally {
                    props.refreshReviews()
                    deletionDialogIsOpen = false
                }
            }
        }
    }

    fun update(){
        if (userContext?.user?.id == props.review.userId){
            scope.launch {
                try {
                    editReview(props.review.id, newRating, newReview, userContext.user!!.authToken!!)
                }finally {
                    props.refreshReviews()
                    editDialogIsOpen = false
                }
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
                    if (props.showMovieTitle){
                        NavLink{
                            to = "/movies/${movie?.id}"
                            css {
                                textDecoration = None.none
                            }
                            Typography{
                                variant = TypographyVariant.h5
                                +movie?.title!!
                            }
                        }
                    }
                    Box{
                        sx {
                            display = Display.flex
                        }
                        Avatar{
                            alt = "account icon"
                        }
                        NavLink{
                            to = "/user/${user?.id}/reviews"
                            css {
                                textDecoration = None.none
                            }
                            Typography {
                                sx {
                                    padding = 9.px
                                }
                                +user?.username!!
                            }
                        }
                        if (userContext?.user?.id == user?.id){
                            Button{
                                sx {
                                    minWidth = 24.px
                                    minHeight = 24.px
                                    padding = 4.px
                                    fontSize = 12.px
                                }
                                variant = ButtonVariant.text
                                color = ButtonColor.primary
                                size = Size.small
                                onClick = {editDialogIsOpen = true}
                                +"Edit"
                            }
                            Button{
                                sx {
                                    minWidth = 24.px
                                    minHeight = 24.px
                                    padding = 4.px
                                    fontSize = 12.px
                                }
                                variant = ButtonVariant.text
                                color = ButtonColor.error
                                size = Size.small
                                onClick = {deletionDialogIsOpen = true}
                                +"Delete"
                            }
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
            Dialog{
                open = deletionDialogIsOpen
                onClose = {_,_ ->deletionDialogIsOpen = false}
                DialogTitle{
                    +"Delete Review"
                }
                DialogContent{
                    +"Are you sure you want to delete this review?"
                }
                DialogActions{
                    Button{
                        onClick = {deletionDialogIsOpen = false}
                        +"Cancel"
                    }
                    Button{
                        onClick = {delete()}
                        +"Delete"
                    }
                }
            }
            Dialog{
                open = editDialogIsOpen
                Box {
                    sx {
                        margin = 2.rem
                    }

                    Rating {
                        sx {
                            marginTop = 1.rem
                        }
                        name = "rating"
                        value = newRating
                        max = 10
                        onChange = { _, newValue ->
                            newRating = (newValue as Int?)!!
                        }
                    }
                    TextField {
                        label = ReactNode("Your review")
                        variant = FormControlVariant.outlined
                        multiline = true
                        ariaReadOnly = false
                        fullWidth = true
                        rows = 5
                        value = newReview
                        onChange = { event ->
                            val target = event.target
                            val value = target.asDynamic().value as String? ?: ""
                            newReview = value
                        }
                    }
                    Button {
                        onClick = {
                            editDialogIsOpen = false
                            newRating = props.review.rating
                            newReview = props.review.review
                        }
                        +"Cancel"
                    }
                    Button {
                        onClick = { _ -> update() }
                        +"Update"
                    }
                }
            }
        } else {
            +"Error loading review"
        }
    }
}