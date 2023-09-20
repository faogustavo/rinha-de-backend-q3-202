package dev.valvassori.rinha.cache.datasource

import dev.valvassori.rinha.cache.RedisCacheStorage
import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.helpers.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

private const val PERSON_ID_PREFIX = "person::id::"
private const val PERSON_NICK_PREFIX = "person::nick::"

internal class PersonCacheImpl(
    private val connection: RedisCacheStorage,
) : PersonCache {
    override suspend fun exists(nick: String): Boolean = withContext(Dispatchers.IO) {
        connection[PERSON_NICK_PREFIX + nick] != null
    }

    override suspend fun getPerson(id: String): Person? = withContext(Dispatchers.IO) {
        connection[PERSON_ID_PREFIX + id]?.let(JsonParser::decodeFromString)
    }

    override suspend fun savePerson(person: Person) = withContext(Dispatchers.IO) {
        connection[PERSON_ID_PREFIX + person.id] = JsonParser.encodeToString(person)
        connection[PERSON_NICK_PREFIX + person.nick] = person.id
    }
}