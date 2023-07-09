package services

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

object jsonClient {
    val client = HttpClient {
        install(ContentNegotiation){
            json()
        }
    }
}