package dev.valvassori.rinha.helpers

object Env {
    val DATABASE_URL: String = getEnvOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/rinha")
    val DATABASE_DRIVER: String = getEnvOrDefault("DATABASE_DRIVER", "org.postgresql.Driver")
    val DATABASE_USER: String = getEnvOrDefault("DATABASE_USER", "rinha")
    val DATABASE_PASSWORD: String = getEnvOrDefault("DATABASE_PASSWORD", "rinha")

    private fun getEnvOrDefault(key: String, default: String): String = try {
        System.getenv(key).takeUnless { it.isBlank() }
    } catch (e: Exception) {
        null
    } ?: default
}