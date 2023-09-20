package dev.valvassori.rinha.datasource

import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson

interface PersonDAO {
    // For extension purposes
    companion object {}

    suspend fun getById(id: String): Person?
    suspend fun create(newPerson: NewPerson): Person
    suspend fun find(term: String): List<Person>
    suspend fun count(): Long
}