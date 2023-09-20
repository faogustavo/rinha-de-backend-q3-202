package dev.valvassori.rinha.repository

import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson

interface PersonRepository {
    // For extension purposes
    companion object {}

    suspend fun getById(id: String): Person?
    suspend fun create(newPerson: NewPerson): Person
    suspend fun find(term: String): List<Person>
    suspend fun count(): Long
}