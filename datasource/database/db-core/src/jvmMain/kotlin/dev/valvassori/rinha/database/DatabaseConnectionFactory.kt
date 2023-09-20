package dev.valvassori.rinha.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import dev.valvassori.rinha.Env

object DatabaseConnectionFactory {
    fun newDataSource(): HikariDataSource {
        val config = HikariConfig()

        config.driverClassName = Env.getEnvOrDefault("DATABASE_DRIVER", "org.postgresql.Driver")
        config.jdbcUrl = Env.getEnvOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/rinha")
        config.username = Env.getEnvOrDefault("DATABASE_USER", "rinha")
        config.password = Env.getEnvOrDefault("DATABASE_PASSWORD", "rinha")

        config.isAutoCommit = true
        config.transactionIsolation = IsolationLevel.TRANSACTION_READ_COMMITTED.name
        config.maximumPoolSize = Env.getEnvOrNull("MAX_POOL_SIZE")?.toIntOrNull() ?: 8
        config.addDataSourceProperty("reWriteBatchedInserts", "true")

        return HikariDataSource(config)
    }
}