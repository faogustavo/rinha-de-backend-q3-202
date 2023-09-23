package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.DBConnection
import dev.valvassori.rinha.database.entity.People
import dev.valvassori.rinha.database.ext.ilike
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.ext.fromDB
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

internal class PersonDAOImpl(private val connection: DBConnection) : PersonDAO {
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
                it[search] = buildList {
                    add(person.nick)
                    add(person.name)
                    addAll(person.stack.orEmpty())
                }.joinToString(" ")
            }
        }
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery {
        People.select { People.search ilike "%$term%" }.limit(50).map(Person.Companion::fromDB)
    }

    override suspend fun count(): Long = connection.dbQuery {
        People.slice(People.id.count())
            .selectAll()
            .single()[People.id.count()]
    }
}