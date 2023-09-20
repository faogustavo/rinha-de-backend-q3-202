package dev.valvassori.rinha.factory

import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.repository.PersonRepository
import dev.valvassori.rinha.repository.PersonRepositoryImpl

object PersonRepositoryFactory {
    fun newInstance(
        dao: PersonDAO,
        cache: PersonCache,
    ): PersonRepository = PersonRepositoryImpl(dao, cache)
}