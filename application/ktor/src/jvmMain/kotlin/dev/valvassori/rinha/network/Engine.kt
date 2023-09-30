package dev.valvassori.rinha.network

import dev.valvassori.rinha.Env

enum class Engine {
    CIO, Netty, Jetty, Tomcat;

    fun factory() = when (this) {
        CIO -> io.ktor.server.cio.CIO
        Jetty -> io.ktor.server.jetty.Jetty
        Netty -> io.ktor.server.netty.Netty
        Tomcat -> io.ktor.server.tomcat.Tomcat
    }

    companion object {
        fun fromEnv(): Engine = parse(Env.getEnvOrDefault("KTOR_ENGINE", CIO.name))
        fun parse(value: String): Engine = entries.find { it.name.equals(value, ignoreCase = true) }
            ?: throw IllegalArgumentException(
                "Invalid engine: $value. Supported values are: ${entries.joinToString(", ")}."
            )
    }
}