package dev.valvassori.rinha.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import dev.valvassori.rinha.Env
import dev.valvassori.rinha.env.getConnectionPoolSize

object DatabaseConnectionFactory {
    fun newDataSource(): HikariDataSource {
        val config = HikariConfig()

        config.driverClassName = Env.getEnvOrDefault("DATABASE_DRIVER", "org.postgresql.Driver")
        config.jdbcUrl = Env.getEnvOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/rinha")
        config.username = Env.getEnvOrDefault("DATABASE_USER", "rinha")
        config.password = Env.getEnvOrDefault("DATABASE_PASSWORD", "rinha")

        config.isAutoCommit = true
        config.transactionIsolation = IsolationLevel.TRANSACTION_READ_COMMITTED.name
        config.maximumPoolSize = Env.getConnectionPoolSize()

        // https://stackoverflow.com/a/32969976
        config.leakDetectionThreshold = 30_000 // 30 secs

        // https://github.com/JetBrains/Exposed/wiki/DSL#batch-insert
        config.addDataSourceProperty("reWriteBatchedInserts", "true")

        return HikariDataSource(config)
    }
}