package dev.valvassori.rinha.database

import com.zaxxer.hikari.HikariDataSource
import dev.valvassori.rinha.Env
import dev.valvassori.rinha.database.dao.JDBCPersonDAOImpl
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.env.getConnectionPoolSize
import dev.valvassori.rinha.errors.UnprocessableEntityException
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.Executors

class JDBCConnection internal constructor(private val dataSource: HikariDataSource) {
    companion object {
        val shared: JDBCConnection by lazy {
            JDBCConnection(DatabaseConnectionFactory.newDataSource())
        }
    }

    val personDAO: PersonDAO by lazy { JDBCPersonDAOImpl(this) }

    private val coroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val semaphore = Semaphore(Env.getConnectionPoolSize() * 4)

    suspend fun <T> dbQuery(block: Connection.() -> T): T = semaphore.withPermit {
        withContext(coroutineDispatcher) {
            try {
                dataSource.connection.use { it.block() }
            } catch (e: SQLException) {
                when (e.sqlState) {
                    DBErrorCodes.IntegrityViolation.UNIQUE_VIOLATION ->
                        throw UnprocessableEntityException(e.message ?: "Unique key violation")

                    else -> throw e
                }
            }
        }
    }
}