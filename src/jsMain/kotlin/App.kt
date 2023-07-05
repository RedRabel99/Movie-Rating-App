
import components.MovieList
import csstype.number
import csstype.vh
import kotlinx.coroutines.MainScope
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.createElement
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter

private val scope = MainScope()

val App = FC<Props> {
   div {
       Header{}
       Container{
           sx{
               marginTop = 10.vh
               height =100.vh
           }
           HashRouter {
               Routes {
                   Route {
                       path = "/"
                       element = createElement(MovieList)
                   }
                   Route {
                       path = "/movies/:id"
                       element = createElement(MovieDetail)
                   }
               }
           }
       }
    }
}


val Header = FC<Props> {
    AppBar{
        position =AppBarPosition.fixed
        Toolbar {
         //   NavLink{
              //  to = "/"
                Typography {
                    sx { flexGrow = number(1.0)}
                    variant = TypographyVariant.h6
                    noWrap = true
                    component = div

                    +"Movie Rating App"
                }
           // }

            Tooltip{
                title = ReactNode("test")
                IconButton{
                    ariaLabel = "account of current user"
                    ariaHasPopup = AriaHasPopup.`false`
                    size = Size.large
                    color = IconButtonColor.inherit
                    Avatar{
                        alt = "account icon"
                    }
                }
            }
        }
    }
}