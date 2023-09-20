package dev.valvassori.rinha

object Env {
    fun getEnvOrNull(key: String): String? = getEnvFromVarSystem(key)
    fun getEnvOrDefault(key: String, default: String): String =
        getEnvOrNull(key) ?: default

}

internal expect fun getEnvFromVarSystem(key: String): String?