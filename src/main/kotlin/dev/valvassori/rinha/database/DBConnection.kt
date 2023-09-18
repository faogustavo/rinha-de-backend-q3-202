package dev.valvassori.rinha.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import dev.valvassori.rinha.errors.UnprocessableEntityException
import dev.valvassori.rinha.helpers.Env
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.concurrent.Executors

class DBConnection(private val db: Database) {
    companion object {
        val shared: DBConnection by lazy {
            val config = HikariConfig()

            config.driverClassName = Env.DATABASE_DRIVER
            config.jdbcUrl = Env.DATABASE_URL
            config.username = Env.DATABASE_USER
            config.password = Env.DATABASE_PASSWORD

            config.isAutoCommit = true
            config.transactionIsolation = IsolationLevel.TRANSACTION_READ_COMMITTED.name
            config.maximumPoolSize = Env.MAX_POOL_SIZE
            config.addDataSourceProperty("reWriteBatchedInserts", "true")

            DBConnection(Database.connect(HikariDataSource(config)))
        }
    }

    private val coroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val semaphore = Semaphore(32)

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(coroutineDispatcher, db) {
        try {
            semaphore.withPermit { block() }
        } catch (e: ExposedSQLException) {
            when (e.sqlState) {
                DBErrorCodes.IntegrityViolation.UNIQUE_VIOLATION ->
                    throw UnprocessableEntityException(e.message ?: "Unique key violation")

                else -> throw e
            }
        }
    }
}