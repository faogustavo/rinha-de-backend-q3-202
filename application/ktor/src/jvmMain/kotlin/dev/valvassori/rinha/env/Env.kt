package dev.valvassori.rinha.env

import dev.valvassori.rinha.Env

val Env.ENGINE: Engine get() = Engine.parse(getEnvOrDefault("ENGINE", Engine.CIO.name))

enum class Engine {
    CIO, Netty, Jetty, Tomcat;

    companion object {
        fun parse(value: String): Engine = entries.find { it.name.equals(value, ignoreCase = true) }
            ?: throw IllegalArgumentException(
                "Invalid engine: $value. Supported values are: ${entries.joinToString(", ")}."
            )
    }
}