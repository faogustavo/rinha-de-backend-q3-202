package dev.valvassori.rinha.cache.datasource

import dev.valvassori.rinha.cache.RedisCacheConnection
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.helpers.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

interface PersonCacheDataSource {
    companion object {
        val shared: PersonCacheDataSource by lazy { PersonCacheDataSourceImpl(RedisCacheConnection.shared) }
    }

    suspend fun exists(nick: String): Boolean
    suspend fun getPerson(id: String): Person?

    suspend fun savePerson(person: Person)
}

private const val PERSON_ID_PREFIX = "person::id::"
private const val PERSON_NICK_PREFIX = "person::nick::"

class PersonCacheDataSourceImpl(
    private val connection: RedisCacheConnection,
) : PersonCacheDataSource {
    override suspend fun exists(nick: String): Boolean = withContext(Dispatchers.IO) {
        connection[PERSON_NICK_PREFIX + nick] != null
    }

    override suspend fun getPerson(id: String): Person? = withContext(Dispatchers.IO) {
        connection[PERSON_ID_PREFIX + id]?.let(JsonParser::decodeFromString)
    }

    override suspend fun savePerson(person: Person) = withContext(Dispatchers.IO) {
        connection[PERSON_ID_PREFIX + person.id] = JsonParser.encodeToString(person)
        connection[PERSON_NICK_PREFIX + person.nick] = person.id.toString()
    }
}