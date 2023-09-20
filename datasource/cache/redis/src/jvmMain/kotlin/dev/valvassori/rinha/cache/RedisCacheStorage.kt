package dev.valvassori.rinha.cache

import dev.valvassori.rinha.Env
import dev.valvassori.rinha.cache.datasource.PersonCacheImpl
import dev.valvassori.rinha.datasource.PersonCache
import redis.clients.jedis.JedisPool

class RedisCacheStorage(private val pool: JedisPool) {
    companion object {
        val shared by lazy {
            RedisCacheStorage(
                JedisPool(
                    Env.getEnvOrDefault("REDIS_URL", "localhost"),
                    Env.getEnvOrNull("REDIS_PORT")?.toIntOrNull() ?: 6379,
                )
            )
        }
    }

    val personCache: PersonCache by lazy { PersonCacheImpl(this) }

    operator fun get(key: String): String? =
        pool.resource.use { it.get(key) }

    operator fun set(key: String, value: String) =
        pool.resource.use { it.set(key, value) }
}