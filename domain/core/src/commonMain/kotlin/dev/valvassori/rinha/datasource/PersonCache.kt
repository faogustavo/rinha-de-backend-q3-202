package dev.valvassori.rinha.datasource

import dev.valvassori.rinha.domain.model.Person

interface PersonCache {
    // For extension purposes
    companion object {}

    suspend fun exists(nick: String): Boolean
    suspend fun getPerson(id: String): Person?
    suspend fun savePerson(person: Person)
}