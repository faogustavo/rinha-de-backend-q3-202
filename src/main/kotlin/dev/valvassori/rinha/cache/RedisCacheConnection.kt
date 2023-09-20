package dev.valvassori.rinha.cache

import dev.valvassori.rinha.helpers.Env
import redis.clients.jedis.JedisPool

class RedisCacheConnection(private val pool: JedisPool) {
    companion object {
        val shared by lazy { RedisCacheConnection(JedisPool(Env.REDIS_CONN_URL, Env.REDIS_PORT)) }
    }

    operator fun get(key: String): String? =
        pool.resource.use { it.get(key) }

    operator fun set(key: String, value: String) =
        pool.resource.use { it.set(key, value) }
}