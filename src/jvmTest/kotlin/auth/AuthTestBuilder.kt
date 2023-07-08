package auth
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
import me.user.application.data.models.UserTable
import params.CreateLoginParams
import params.CreateUserParams
import models.User
import org.jetbrains.exposed.sql.deleteAll
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


open class AuthTestBuilder private constructor() {
    private lateinit var server: TestApplicationEngine
    private lateinit var response: HttpResponse
    private lateinit var json: JsonElement
    companion object{
        fun given(): Given {
            return AuthTestBuilder().buildGiven()
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

        fun `when`(): When {
            return When()
        }
    }

    inner class When{
        fun postRequestOnAuthRegisterIsSent(username: String, email: String, password: String): When {
            runBlocking {
                response = server.client.post("auth/register"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(CreateUserParams.serializer(), CreateUserParams(username, email, password)))
            }
                json = Json.decodeFromString(JsonElement.serializer(), response.bodyAsText())
            }

            return this
        }

        fun postRequestOnAuthLoginWithGivenCredentials(email: String, password: String): When{
            runBlocking {
                response = server.client.post("auth/login"){
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(
                        CreateLoginParams.serializer(),
                        CreateLoginParams(email, password)
                    ))
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

        fun credentialsAreValid(): Then {
            assertEquals(HttpStatusCode.OK, response.status)
            return this
        }

        fun credentialsAreInvalid(): Then {
            assertEquals(HttpStatusCode.Unauthorized, response.status)
            return this
        }

        fun shouldReturnToken(): Then {
            val user = fromResponseJsonToUser()
            assertNotNull(user.authToken)
            return this
        }
        fun responseStatusCodeIsCreated(): Then {
            assertEquals(HttpStatusCode.Created, response.status)
            return this
        }

        fun responseUsernameShouldBe(username: String): Then {
            val user = fromResponseJsonToUser()
            assertEquals(username, user.username)
            return this
        }

        fun responseEmailShouldBe(email: String): Then {
            val user = fromResponseJsonToUser()
            assertEquals(email, user.email)
            return this
        }

        private fun fromResponseJsonToUser(): User{
            val data = json.jsonObject["data"]
            assertNotNull(data)
            return Json.decodeFromJsonElement<User>(data)
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
                    UserTable.deleteAll()
                }
            }
        }
    }
}