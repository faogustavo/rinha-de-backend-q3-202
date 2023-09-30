package dev.valvassori.rinha

import dev.valvassori.rinha.cache.RedisCacheStorage
import dev.valvassori.rinha.cache.datasource.NoopPersonCache
import dev.valvassori.rinha.database.ExposedDBConnection
import dev.valvassori.rinha.database.JDBCConnection
import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.factory.PersonRepositoryFactory

object ApplicationDependencies {
    private val cache: PersonCache by lazy {
        if (CoreApplicationEnv.cacheEnabled) {
            RedisCacheStorage.shared.personCache
        } else {
            NoopPersonCache
        }
    }

    private val dao: PersonDAO by lazy {
        if (CoreApplicationEnv.ormEnabled) {
            ExposedDBConnection.shared.personDAO
        } else {
            JDBCConnection.shared.personDAO
        }
    }

    val repository by lazy {
        PersonRepositoryFactory.newInstance(dao, cache)
    }
}