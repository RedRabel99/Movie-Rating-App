
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import models.Movie

val jsonClient = HttpClient {
    install(ContentNegotiation){
        json()
    }
}



suspend fun getMovieList(): List<Movie> {
    val x = jsonClient.get("/movies").body<SyncResponse<List<Movie>>>()
    return x.data?: emptyList()

}

//suspend fun getMovieList(): List<Movie> {
//    val response = jsonClient.get("/movies").body<BaseResponse<Any>>()
//    return when (response) {
//        is BaseResponse.SuccessResponse -> {
//            val movie = response.data as? Movie
//            if (movie != null) {
//                listOf(movie)
//            } else {
//                emptyList()
//            }
//        }
//        is BaseResponse.ErrorResponse -> {
//            // Obsługa błędu, jeśli jest to konieczne
//            emptyList()
//        }
//        else -> emptyList()
//    }
//}
