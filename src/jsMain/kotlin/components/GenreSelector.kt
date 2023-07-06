package components

import csstype.Display
import csstype.JustifyContent
import csstype.pct
import models.Genre
import mui.material.Button
import mui.material.ButtonGroup
import mui.material.ButtonVariant
import mui.material.Grid
import mui.system.sx
import react.FC
import react.Props
import react.key
import react.useState
import utils.md
import utils.xs

external interface GenreSelectorProps : Props {
    var genres: List<Genre>
    var selectedGenre: String?
    //var movieList = List<Movie>
    var onGenreSelect: (a: String) -> Unit
}

val GenreSelector = FC<GenreSelectorProps>{props ->
    var selected by useState(props.selectedGenre)
    fun handleGenreClick(genreId: String) {
        props.selectedGenre = genreId
        selected = genreId
        console.log("GenreSelector: $genreId")
        props.onGenreSelect(genreId)
    }
    ButtonGroup{
        Grid{
            container = true
            //spacing = responsive(1)
            Grid{
                item = true
                md = 2
                xs = 4
                Button{
                    key = "all"
                    variant = if(selected == "") ButtonVariant.contained else ButtonVariant.outlined
                    sx {
                        width = 100.pct// Set the desired width
                        height = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    onClick = {handleGenreClick("")}
                    +"All"
                }
            }
            props.genres.forEach{genre ->
                Grid{
                    item = true
                    md = 2
                    xs = 4
                    Button{
                        key = genre.id.toString()
                        variant = if(genre.name == selected) ButtonVariant.contained else ButtonVariant.outlined
                        sx {
                            width = 100.pct// Set the desired width
                            height = 100.pct
                            display = Display.flex
                            justifyContent = JustifyContent.center
                        }
                        onClick = {handleGenreClick(genre.name)}
                        +genre.name
                    }
                }

            }
        }

    }
}