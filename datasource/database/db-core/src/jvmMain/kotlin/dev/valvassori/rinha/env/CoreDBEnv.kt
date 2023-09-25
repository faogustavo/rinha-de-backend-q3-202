package dev.valvassori.rinha.env

import dev.valvassori.rinha.Env

fun Env.getConnectionPoolSize(): Int = getInt("MAX_POOL_SIZE", 8)