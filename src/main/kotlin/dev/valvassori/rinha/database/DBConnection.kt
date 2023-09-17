package dev.valvassori.rinha.database

import dev.valvassori.rinha.errors.UnprocessableEntityException
import dev.valvassori.rinha.helpers.Env
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBConnection(private val db: Database) {
    companion object {
        val shared: DBConnection by lazy {
            DBConnection(
                Database.connect(
                    url = Env.DATABASE_URL,
                    driver = Env.DATABASE_DRIVER,
                    user = Env.DATABASE_USER,
                    password = Env.DATABASE_PASSWORD,
                )
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO, db) {
        try {
            block()
        } catch (e: ExposedSQLException) {
            when (e.sqlState) {
                DBErrorCodes.IntegrityViolation.UNIQUE_VIOLATION ->
                    throw UnprocessableEntityException(e.message ?: "Unique key violation")

                else -> throw e
            }
        }
    }
}