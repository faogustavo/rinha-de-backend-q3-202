package dev.valvassori.rinha.helpers

object Env {
    val DATABASE_URL: String = getEnvOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/rinha")
    val DATABASE_DRIVER: String = getEnvOrDefault("DATABASE_DRIVER", "org.postgresql.Driver")
    val DATABASE_USER: String = getEnvOrDefault("DATABASE_USER", "rinha")
    val DATABASE_PASSWORD: String = getEnvOrDefault("DATABASE_PASSWORD", "rinha")

    val REDIS_CONN_URL: String = getEnvOrDefault("REDIS_URL", "localhost")
    val REDIS_PORT: Int = getEnvOrNull("REDIS_PORT")?.toIntOrNull() ?: 6379

    val MAX_POOL_SIZE: Int = getEnvOrNull("MAX_POOL_SIZE")?.toIntOrNull() ?: 8
    val ENGINE: Engine = Engine.parse(getEnvOrDefault("ENGINE", Engine.CIO.name))

    private fun getEnvOrNull(key: String) = try {
        System.getenv(key).takeUnless { it.isBlank() }
    } catch (e: Exception) {
        null
    }

    enum class Engine {
        CIO, Netty, Jetty, Tomcat;

        companion object {
            fun parse(value: String): Engine = values().find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException(
                    "Invalid engine: $value. Supported values are: ${values().joinToString(", ")}"
                )
        }
    }

    private fun getEnvOrDefault(key: String, default: String): String =
        getEnvOrNull(key) ?: default
}