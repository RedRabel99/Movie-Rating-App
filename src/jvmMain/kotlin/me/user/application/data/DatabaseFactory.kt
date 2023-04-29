package me.user.application.data

import me.user.application.data.models.UserTable
import kotlinx.coroutines.Dispatchers
import me.user.application.data.models.GenresTable
import me.user.application.data.models.MovieGenresTable
import me.user.application.data.models.MovieTable
import models.Movie
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDateTime

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:./database.db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.drop(MovieTable)
            SchemaUtils.create(MovieTable, GenresTable, MovieGenresTable)
            SchemaUtils.createMissingTablesAndColumns(MovieTable)

            MovieTable.insert {
                it[title] = "title"
                it[runtime] = 90
                it[score] = BigDecimal(2.5)
                it[review_count] = 50
                it[overview] = "SUPER FILM SERIO POLECAM TO OPIS JEST"
                it[release_date] = LocalDateTime.now()
                it[poster_path] = "plakat"

            }
//            GenresTable.insert {
//                it[name] = "action"
//            }
//            GenresTable.insert {
//                it[name] = "comedy"
//            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}