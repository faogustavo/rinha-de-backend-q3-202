package dev.valvassori.rinha.repository

import dev.valvassori.rinha.cache.datasource.PersonCacheDataSource
import dev.valvassori.rinha.database.dao.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.errors.UnprocessableEntityException
import java.util.UUID

interface PersonRepository {
    companion object {
        val shared: PersonRepository by lazy { PersonRepositoryImpl(PersonDAO.shared, PersonCacheDataSource.shared) }
    }

    suspend fun getById(id: UUID): Person?
    suspend fun create(newPerson: NewPerson): Person
    suspend fun find(term: String): List<Person>
    suspend fun count(): Long
}


class PersonRepositoryImpl(
    private val dao: PersonDAO,
    private val cache: PersonCacheDataSource,
) : PersonRepository {
    override suspend fun getById(id: UUID): Person? = cache.getPerson(id.toString())
        ?: dao.getById(id)?.also { cache.savePerson(it) }

    override suspend fun create(newPerson: NewPerson): Person {
        if (cache.exists(newPerson.nick)) {
            throw UnprocessableEntityException("Duplicate key value violates unique constraint")
        }

        return dao.create(newPerson)
            .also { cache.savePerson(it) }
    }

    override suspend fun find(term: String): List<Person> = dao.find(term)
    override suspend fun count(): Long = dao.count()
}