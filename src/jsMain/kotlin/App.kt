
import components.Header
import components.MovieList
import csstype.vh
import kotlinx.coroutines.MainScope
import mui.material.Box
import mui.material.Container
import mui.system.sx
import react.FC
import react.Props
import react.createElement
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter

private val scope = MainScope()

val App = FC<Props> {
   Box {
       Header{}
       Container{
           sx{
               marginTop = 10.vh
               height =100.vh
           }
           HashRouter {
               Routes {
                   Route {
                       path = "/movies/:id"
                       element = createElement(MovieDetail)
                   }
                   Route{
                       path = "/"
                       element = createElement(MovieList)
                   }
               }
           }
       }
    }
}