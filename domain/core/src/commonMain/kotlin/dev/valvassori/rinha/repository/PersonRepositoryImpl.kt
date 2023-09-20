package dev.valvassori.rinha.repository

import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.errors.UnprocessableEntityException

internal class PersonRepositoryImpl(
    private val dao: PersonDAO,
    private val cache: PersonCache,
) : PersonRepository {
    override suspend fun getById(id: String): Person? = cache.getPerson(id)
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