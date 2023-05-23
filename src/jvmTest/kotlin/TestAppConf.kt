import io.ktor.server.application.*
import me.user.application.config.configureContentNegotiation
import me.user.application.config.configureRouting
import me.user.application.data.models.*
import me.user.application.routes.auth.params.CreateUserParams
import me.user.application.security.configureSecurity
import me.user.application.security.hashPassword
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseFactory {
    val initialUser = CreateUserParams("username1", "user1@mail.com", "1234")
    fun init(){
        Database.connect(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "",
            password = "")
        transaction{
            //drop all tables to make sure we start with a clean slate
            SchemaUtils.drop(MovieTable, GenresTable, MovieGenresTable, UserTable, ReviewTable)
            SchemaUtils.create(MovieTable, GenresTable, MovieGenresTable, UserTable, ReviewTable)

            UserTable.insert {
                it[username] = initialUser.username
                it[email] = initialUser.email
                it[password] = hashPassword(initialUser.password)
            }
        }
    }
}

fun Application.conf() {
    TestDatabaseFactory.init()
    configureContentNegotiation()
    configureSecurity()
    configureRouting()
}

