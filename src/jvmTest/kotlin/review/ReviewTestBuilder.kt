package review

import TestDatabaseFactory
import conf
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import me.user.application.data.DatabaseFactory.dbQuery
import me.user.application.data.models.MovieTable
import me.user.application.data.models.ReviewTable
import me.user.application.data.models.UserTable
import me.user.application.routes.auth.params.CreateLoginParams
import me.user.application.routes.review.params.CreateReviewParams
import models.Movie
import models.Review
import models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class ReviewTestBuilder private constructor(){
    private lateinit var server: TestApplicationEngine
    private lateinit var response: HttpResponse
    private lateinit var json: JsonElement
    private lateinit var bearerToken: String
    private var movieId = 1

    companion object{
        fun given(): Given {
            return ReviewTestBuilder().buildGiven()
        }

    }
    private fun buildGiven(): Given {
        return Given()
    }

    inner class Given{
        fun thereIsConfiguredClient(): Given {
            //create TestApplicationEngine to allow access to client property outside of this test block
            server = TestApplicationEngine(createTestEnvironment{
                developmentMode = false
                module {
                    conf()
                }
            })
            server.start(wait = false)
            return this
        }

        fun thereIsMovieInDatabase(): Given {
            runBlocking {
                var statement: InsertStatement<Number>? = null
                statement = dbQuery{
                    MovieTable.insert {
                        it[MovieTable.title] = "TestMovie"
                        it[MovieTable.review_count] = 0
                        it[MovieTable.score] = 0.0f
                        it[MovieTable.release_date] = java.time.LocalDateTime.now()
                        it[MovieTable.overview] = "TestOverview"
                        it[MovieTable.poster_path] = "TestPosterPath"
                    }
                }
                movieId = statement.resultedValues?.get(0)?.let { resultRowToMovie(it) }?.id ?: 0

            }

            return this
        }

        fun thereIsALoggedUser(): Given {

            runBlocking {


                response = server.client.post("auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Json.encodeToString(
                            CreateLoginParams.serializer(),
                            CreateLoginParams(
                                TestDatabaseFactory.initialUser.email,
                                TestDatabaseFactory.initialUser.password
                            )
                        )
                    )
                }
                json = Json.decodeFromString(JsonElement.serializer(), response.bodyAsText())
                bearerToken = fromResponseJsonToUser(json).authToken ?: ""
            }

                return this

        }
        private fun fromResponseJsonToUser(cjson: JsonElement = json): User {
            val data = json.jsonObject["data"]
            assertNotNull(data)
            return Json.decodeFromJsonElement<User>(data)
        }

        private fun resultRowToMovie(row: ResultRow): Movie?{
            return try {
                Movie(
                    id = row[MovieTable.id],
                    title = row[MovieTable.title],
                    reviewCount = row[MovieTable.review_count],
                    score = row[MovieTable.score].toFloat(),
                    releaseDate = row[MovieTable.release_date].toString(),
                    //runtime = row[MovieTable.runtime],
                    overview = row[MovieTable.overview],
                    posterPath = row[MovieTable.poster_path]

                )
            }catch (e: Exception){
                null
            }
        }
        fun `when`(): When {
            return When()
        }
    }

    inner class When{

        fun postRequestOnReviewCreateIsSent(review: String, score: Int): When {
            runBlocking {
                response = server.client.post("reviews"){
                    contentType(ContentType.Application.Json)
                    setBody(
                        Json.encodeToString(
                            CreateReviewParams.serializer(),
                            CreateReviewParams(movieId, 1, score, review)))
                    bearerAuth(bearerToken)
                }
                json = Json.decodeFromString(JsonElement.serializer(), response.bodyAsText())
            }
            return this
        }
        fun then(): Then {
            return Then()
        }
    }

    inner class Then{

        fun responseStatusCodeIsCreated(): Then {
            assertEquals(HttpStatusCode.Created, response.status)
            return this
        }

        fun reviewMovieIdShouldBeCorrect(): Then {
            val responseMovieId = fromResponseJsonToReview().id

            assertEquals(movieId, responseMovieId)
            return this
        }
        private fun fromResponseJsonToReview(): Review {
            val data = json.jsonObject["data"]
            assertNotNull(data)
            return Json.decodeFromJsonElement<Review>(data)
        }
        fun finally(): Finally {
            return Finally()
        }
    }

    inner class Finally{
        fun stopServer(){
            server.stop()
            runBlocking{
                dbQuery {
                    ReviewTable.deleteAll()
                    MovieTable.deleteAll()
                    UserTable.deleteAll()

                }
            }
        }
    }
}

