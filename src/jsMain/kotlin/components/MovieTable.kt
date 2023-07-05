package components

import csstype.AlignItems
import csstype.Display
import csstype.None
import csstype.px
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
        props.movieList = when (sortKey) {
            "title" -> if (sortAscending) props.movieList.sortedBy { it.title } else props.movieList.sortedByDescending { it.title }
            "score" -> if (sortAscending) props.movieList.sortedBy { it.score } else props.movieList.sortedByDescending { it.score }
            "reviewCount" -> if (sortAscending) props.movieList.sortedBy { it.reviewCount } else props.movieList.sortedByDescending { it.reviewCount }
            else -> props.movieList
        }
    }

//    fun handleChangePage(newPage: Int) {
//        page = newPage
//    }
//
//    fun handleChangeRowsPerPage(event: ChangeEvent<HTMLElement>) {
//        val target = event.target as? HTMLElement
//        val value = target?.asDynamic()?.value?.toIntOrNull()
//
//        if (value != null) {
//            // Use the value as needed
//            rowsPerPage = value
//            page = 0
//        }
//    }

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
                            Typography {
                                variant = TypographyVariant.h5
                                +"Title"
                            }
                        }
                        TableCell {
                            Typography {
                                variant = TypographyVariant.h5
                                +"Score"
                            }
                        }
                        TableCell {
                            Typography {
                                variant = TypographyVariant.h5
                                +"Review Count"
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
                                        +"${item.score}"
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
        ReactHTML.select {
            value = sortKey
            onChange = { event ->
                sortKey = (event.target).value
                sortMovieList()
            }
            ReactHTML.option { +"title" }
            ReactHTML.option { +"score" }
            ReactHTML.option { +"reviewCount" }
        }
//        TablePagination{
//            rowsPerPageOptions=arrayOf(5, 10, 25)
//            component = div
//            count = props.movieList.size
//            this.rowsPerPage = rowsPerPage
//            this.page = page
//            onPageChange = {_, newPage -> handleChangePage(newPage.toInt())}
//            onRowsPerPageChange = {event -> handleChangeRowsPerPage(event)}
//        }
        // Sorting order buttons
//        ReactHTML.button {
//            onClick = {
//                sortAscending = !sortAscending
//                sortMovieList()
//            }
//            if (sortAscending) {
//                +"Sort Ascending"
//            } else {
//                +"Sort Descending"
//            }
//        }
    }
}