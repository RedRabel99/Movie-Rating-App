
import components.MovieList
import kotlinx.coroutines.MainScope
import react.FC
import react.Props
import react.createElement
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter

private val scope = MainScope()

val App = FC<Props> {
    HashRouter {
        Routes {
            Route {
                path = "/"
                element = createElement(MovieList)

            }
            Route{
                path = "/movies/:id"
                element = createElement(MovieDetail)
            }
        }
    }
}
