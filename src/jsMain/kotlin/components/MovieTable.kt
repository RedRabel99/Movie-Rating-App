package components

import csstype.*
import emotion.react.css
import models.Movie
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.useState
import kotlin.math.round


external interface MovieTableProps: Props {
    var movieList: List<Movie>
}

val MovieTable = FC<MovieTableProps>{props ->
    var sortKey by useState("title") // Default sorting key
    var sortAscending by useState(true) // Default sorting order
    var rowsPerPage by useState(10) // Default rows per
    var page by useState(0) // Default page number
    var order by useState("asc") // Default order
    var orderBy by useState("title") // Default order by

    fun sortMovieList() {
        props.movieList = when (orderBy) {
            "title" -> if (order == "asc") props.movieList.sortedBy { it.title } else props.movieList.sortedByDescending { it.title }
            "score" -> if (order == "asc") props.movieList.sortedBy { it.score } else props.movieList.sortedByDescending { it.score }
            "reviewCount" -> if (order == "asc") props.movieList.sortedBy { it.reviewCount } else props.movieList.sortedByDescending { it.reviewCount }
            else -> props.movieList
        }
    }

    fun handleSortRequest(property: String) {
        val isAscending = orderBy == property && order == "asc"
        val newOrder = if (isAscending) "desc" else "asc"
        order = newOrder
        orderBy = property
        sortMovieList()
    }

    Box{
        TableContainer {
            component = Paper
            Table {
                TableHead {
                    TableRow {
                        TableCell {
                            Typography {
                                variant = TypographyVariant.h5
                                +"Poster"
                            }
                        }
                        TableCell {
                            Button{
                                onClick = { handleSortRequest("title") }
                                sx{
                                    fontWeight = FontWeight.bold
                                }
                                color = if (orderBy == "title") ButtonColor.primary else ButtonColor.secondary
                                Typography {
                                    variant = TypographyVariant.h5
                                    +"Title"
                                }
                            }
                        }
                        TableCell {
                            Button{
                                onClick = { handleSortRequest("score") }
                                sx{
                                    fontWeight = FontWeight.bold
                                }
                                color = if (orderBy == "score") ButtonColor.primary else ButtonColor.secondary
                                Typography {
                                    variant = TypographyVariant.h5
                                    +"Score"
                                }
                            }
                        }
                        TableCell {
                            Button{
                                onClick = { handleSortRequest("reviewCount") }
                                sx{
                                    fontWeight = FontWeight.bold
                                }
                                color = if (orderBy == "reviewCount") ButtonColor.primary else ButtonColor.secondary
                                Typography {
                                    variant = TypographyVariant.h5
                                    +"Review Count"
                                }
                            }
                        }
                    }
                }
                TableBody {
                    props.movieList.forEach { item ->
                        TableRow {
                            TableCell {
                                Box {
                                    sx {
                                        display = Display.flex
                                        alignItems = AlignItems.center
                                    }
                                    ReactHTML.img {
                                        src = "https://image.tmdb.org/t/p/w300_and_h450_bestv2${item.posterPath}"
                                        css {
                                            width = 80.px
                                            marginRight = 2.px
                                        }
                                    }
                                }
                            }
                            TableCell {
                                NavLink {
                                    to = "/movies/${item.id}"
                                    css {
                                        textDecoration = None.none
                                    }
                                    Typography {
                                        +item.title
                                    }
                                }

                            }
                            TableCell {
                                Box {
                                    sx {
                                        display = Display.flex
                                        alignItems = AlignItems.center
                                    }
                                    Typography {
                                        +"${round(item.score * 100) / 100}"
                                    }
                                    Rating {
                                        name = "read-only"
                                        value = 1
                                        max = 1
                                        readOnly = true

                                    }
                                }

                            }
                            TableCell {
                                Typography {
                                    +"${item.reviewCount}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}