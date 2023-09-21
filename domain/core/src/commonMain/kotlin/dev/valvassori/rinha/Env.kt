package dev.valvassori.rinha

object Env {
    fun getBoolean(key: String, defaultValue: Boolean = false) =
        getEnvOrNull(key)?.toBooleanStrictOrNull() ?: defaultValue

    fun getInt(key: String, defaultValue: Int) =
        getEnvOrNull(key)?.toIntOrNull() ?: defaultValue

    fun getEnvOrNull(key: String): String? = getEnvFromVarSystem(key)
    fun getEnvOrDefault(key: String, default: String): String =
        getEnvOrNull(key) ?: default

}

internal expect fun getEnvFromVarSystem(key: String): String?