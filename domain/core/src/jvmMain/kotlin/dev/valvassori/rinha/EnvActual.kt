package dev.valvassori.rinha

internal actual fun getEnvFromVarSystem(key: String): String? = try {
    System.getenv(key).takeUnless { it.isBlank() }
} catch (e: Exception) {
    null
}
