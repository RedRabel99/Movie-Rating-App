package me.user.application.data

import kotlinx.coroutines.Dispatchers
import me.user.application.data.models.GenresTable
import me.user.application.data.models.MovieGenresTable
import me.user.application.data.models.MovieTable
import me.user.application.data.models.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:./database.db"
        Database.connect(jdbcURL, driverClassName)
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(MovieTable, GenresTable, MovieGenresTable)

            populateTables()
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}