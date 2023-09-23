package dev.valvassori.rinha.cache.datasource

import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.domain.model.Person

object NoopPersonCache : PersonCache {
    override suspend fun exists(nick: String): Boolean = false
    override suspend fun getPerson(id: String): Person? = null
    override suspend fun savePerson(person: Person) {}
}