package dev.valvassori.rinha

object CoreApplicationEnv {
    val port = Env.getInt("HTTP_PORT", 8080)
    val returnErrorBody = Env.getBoolean("RETURN_ERROR_BODY")
    internal val cacheEnabled = Env.getBoolean("DISABLE_CACHE").not()
    internal val ormEnabled = Env.getBoolean("DISABLE_EXPOSED").not()
}