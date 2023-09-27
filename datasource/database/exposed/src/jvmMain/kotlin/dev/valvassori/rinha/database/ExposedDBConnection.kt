package dev.valvassori.rinha.database

import dev.valvassori.rinha.Env
import dev.valvassori.rinha.database.dao.ExposedPersonDAOImpl
import dev.valvassori.rinha.database.model.SQLDialect
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.env.getConnectionPoolSize
import dev.valvassori.rinha.errors.UnprocessableEntityException
import dev.valvassori.rinha.ext.dialect
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.Executors

class ExposedDBConnection internal constructor(
    private val db: Database,
    private val dialect: SQLDialect,
) {
    companion object {
        val shared: ExposedDBConnection by lazy {
            val dataSource = DatabaseConnectionFactory.newDataSource()
            ExposedDBConnection(Database.connect(dataSource), dataSource.dialect())
        }
    }

    val personDAO: PersonDAO by lazy { ExposedPersonDAOImpl(this) }

    private val coroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val semaphore = Semaphore(Env.getConnectionPoolSize() * 4)

    suspend fun <T> dbQuery(block: (SQLDialect) -> T): T = semaphore.withPermit {
        try {
            withContext(coroutineDispatcher) {
                transaction(db) { block(dialect) }
            }
        } catch (e: ExposedSQLException) {
            when (e.sqlState) {
                DBErrorCodes.IntegrityViolation.uniqueViolation(dialect) ->
                    throw UnprocessableEntityException(e.message ?: "Unique key violation")

                else -> throw e
            }
        }
    }
}