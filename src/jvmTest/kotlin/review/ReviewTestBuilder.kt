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
import params.CreateReviewParams
import models.Movie
import models.Review
import models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import params.CreateLoginParams
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class ReviewTestBuilder private constructor(){
    private lateinit var server: TestApplicationEngine
    private lateinit var response: HttpResponse
    private lateinit var json: JsonElement
    private var bearerToken = ""
    private var addedMovieId = 1
    private var initialUserId = 1
    private var initialReviewId = 1

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
        fun thereIsReviewInDatabase(movieRating: Int): Given{
            runBlocking {
                dbQuery{
                    val review = ReviewTable.insert {
                        it[ReviewTable.rating] = movieRating
                        it[ReviewTable.review] = "TestReview"
                        it[ReviewTable.userId] = initialUserId
                        it[ReviewTable.movieId] = addedMovieId
                    }

                    initialReviewId = review.resultedValues?.get(0)?.get(ReviewTable.id) as Int
                    MovieTable.update({MovieTable.id eq addedMovieId}){
                        it[MovieTable.review_count] = (MovieTable.review_count + 1)
                        it[MovieTable.rating] = movieRating.toFloat()
                    }
                }
            }
            return this
        }

        fun thereIsMovieInDatabase(): Given {
            runBlocking {
                var statement: InsertStatement<Number>? = null
                statement = dbQuery{
                    MovieTable.insert {
                        it[MovieTable.title] = "TestMovie"
                        it[MovieTable.review_count] = 0
                        it[MovieTable.rating] = 0.0f
                        it[MovieTable.release_date] = java.time.LocalDateTime.now()
                        it[MovieTable.overview] = "TestOverview"
                        it[MovieTable.poster_path] = "TestPosterPath"
                    }
                }
                addedMovieId = statement.resultedValues?.get(0)?.let { resultRowToMovie(it) }?.id ?: 1

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
                initialUserId = fromResponseJsonToUser(json).id
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
                    score = row[MovieTable.rating].toFloat(),
                    releaseDate = row[MovieTable.release_date].toString(),
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
                            CreateReviewParams(addedMovieId, initialUserId, score, review)
                        ))
                    bearerAuth(bearerToken)
                }
                json = Json.decodeFromString(JsonElement.serializer(), response.bodyAsText())
            }
            return this
        }
        fun updateRequestOnReviewIsSent(review: String, rating: Int): When {
            runBlocking {
                response = server.client.patch("reviews/${initialReviewId}"){
                    contentType(ContentType.Application.Json)
                    setBody(
                        """
                            {
                                "review": "$review",
                                "rating": $rating
                            }
                        """
                    )
                    bearerAuth(bearerToken)
                }
            }
            return this
        }


        fun deleteRequestOnReviewIsSent(): When {
            runBlocking {
                response = server.client.delete("reviews/1"){
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

        fun responseStatusCodeIsUnauthorized(): Then {
            assertEquals(HttpStatusCode.Unauthorized, response.status)
            return this
        }
        fun responseStatusCodeIsNoContent(): Then {
            assertEquals(HttpStatusCode.NoContent, response.status)
            return this
        }

        fun responseStatusCodeIsOk(): Then {
            assertEquals(HttpStatusCode.OK, response.status)
            return this
        }

        fun reviewWasDeletedFromDatabase(): Then {
            val review = runBlocking {
                dbQuery {
                    ReviewTable.select { ReviewTable.id eq 1 }.map {
                        it[ReviewTable.id]
                    }.firstOrNull()
                }
            }
            assertEquals(null, review)
            return this
        }

        fun reviewMovieIdShouldBeCorrect(): Then {
            val responseMovieId = fromResponseJsonToReview().id

            assertEquals(addedMovieId, responseMovieId)
            return this
        }

        fun movieRatingShouldBeCorrect(correctRating: Float): Then {
            val responseMovieRating = getMovieScore()

            assertEquals(correctRating, responseMovieRating)
            return this
        }

        private fun fromResponseJsonToReview(): Review {
            val data = json.jsonObject["data"]
            assertNotNull(data)
            return Json.decodeFromJsonElement<Review>(data)
        }
        private fun getMovieScore(): Float {
            return runBlocking {
                dbQuery {
                    MovieTable.select { MovieTable.id eq addedMovieId }.map {
                        it[MovieTable.rating]
                    }.first().toFloat()
                }
            }
        }
        fun finally(): Finally {
            return Finally()
        }
    }

    inner class Finally{
        fun stopServer(){
            server.stop()
        }
    }
}