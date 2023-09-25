package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.ExposedDBConnection
import dev.valvassori.rinha.database.entity.People
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.ext.fromDB
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

internal class ExposedPersonDAOImpl internal constructor(private val connection: ExposedDBConnection) : PersonDAO {
    override suspend fun getById(id: String): Person? = connection.dbQuery {
        People.select { People.id eq UUID.fromString(id) }
            .limit(1)
            .singleOrNull()
            ?.let(Person.Companion::fromDB)
    }

    override suspend fun create(person: Person) {
        connection.dbQuery {
            People.insert {
                it[id] = UUID.fromString(person.id)
                it[nick] = person.nick
                it[name] = person.name
                it[birthDate] = person.birthDate
                it[stack] = person.stack.toTypedArray()
                it[search] = person.search
            }
        }
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery {
        People.select { People.search like "%${term.lowercase()}%" }.limit(50).map(Person.Companion::fromDB)
    }

    override suspend fun count(): Long = connection.dbQuery {
        People.slice(People.id.count())
            .selectAll()
            .single()[People.id.count()]
    }
}