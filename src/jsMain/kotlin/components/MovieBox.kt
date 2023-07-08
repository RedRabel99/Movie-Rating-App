package components

import csstype.*
import emotion.react.css
import models.Movie
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import utils.md
import utils.xs
import kotlin.math.round

external interface MovieBoxProps: Props {
    var movie: Movie
}

val MovieBox = FC<MovieBoxProps> { props ->

    Paper {
        elevation = 3
        sx { padding = 2.rem }
        Grid {
            container = true
            spacing = responsive(2)
            rowSpacing = responsive(4)
            Grid {
                item = true
                xs = 12
                md = 4
                Box {
                    sx {
                        alignItems = AlignItems.center
                    }
                    ReactHTML.img {
                        src = "https://image.tmdb.org/t/p/w300_and_h450_bestv2${props.movie.posterPath}"
                        alt = "${props.movie.title} poster"
                    }
                }
            }
            Grid {
                item = true
                xs = 12
                md = 8
                Grid {
                    container = true
                    direction = responsive(GridDirection.column)
                    Grid {
                        xs = 12
                        Typography {
                            variant = TypographyVariant.h5
                            +props.movie.title
                        }
                    }
                    Grid {
                        xs = 6
                        Box {
                            sx {
                                display = Display.flex
                            }
                            Typography {
                                +"${round(props.movie.score * 100) / 100}"
                            }
                            Rating {
                                name = "read-only"
                                value = 1
                                max = 1
                                readOnly = true

                            }
                        }

                    }
                    Grid{
                        xs = 6
                        Typography {
                            variant = TypographyVariant.body1
                            +"Review Count: ${props.movie.reviewCount}"
                        }
                    }
                    Grid{
                        xs = 12
                        Typography {
                            variant = TypographyVariant.body1
                            +props.movie.overview
                        }
                    }
                    Grid{
                        xs = 12
                        Typography{
                            variant = TypographyVariant.body1
                            +"Genres:"
                            Box{
                                sx {
                                    display = Display.flex
                                    flexWrap = FlexWrap.wrap
                                }
                                Grid{
                                    container = true
                                    spacing = responsive(1)
                                    props.movie.genres.forEach{genre ->
                                        Grid{
                                            item = true
                                            xs = 4
                                            md = 2
                                            NavLink{
                                                to = "/?genre=${genre.name}"
                                                css {
                                                    textDecoration = None.none
                                                }
                                                Typography{
                                                    variant = TypographyVariant.body1
                                                    +genre.name
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
