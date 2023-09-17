package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.DBConnection
import dev.valvassori.rinha.database.entity.People
import dev.valvassori.rinha.database.ext.ilike
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

interface PersonDAO {
    companion object {
        val shared: PersonDAO by lazy { PersonDAOImpl(DBConnection.shared) }
    }
    suspend fun getById(id: UUID): Person?
    suspend fun create(newPerson: NewPerson): Person
    suspend fun find(term: String): List<Person>
    suspend fun count(): Long
}

class PersonDAOImpl(private val connection: DBConnection) : PersonDAO {
    override suspend fun getById(id: UUID): Person? = connection.dbQuery {
        People.select { People.id eq id }
            .limit(1)
            .singleOrNull()
            ?.let(::Person)
    }

    override suspend fun create(newPerson: NewPerson): Person = connection.dbQuery {
        val id = People.insertAndGetId {
            it[nick] = newPerson.nick
            it[name] = newPerson.name
            it[birthDate] = newPerson.birthDate
            it[stack] = newPerson.stack.orEmpty().toTypedArray()
            it[search] = buildList {
                add(newPerson.nick)
                add(newPerson.name)
                addAll(newPerson.stack.orEmpty())
            }.joinToString(" ")
        }

        Person(
            id = id.value.toString(),
            name = newPerson.name,
            nick = newPerson.nick,
            birthDate = newPerson.birthDate,
            stack = newPerson.stack.orEmpty()
        )
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery {
        People.select { People.search ilike "%$term%" }.limit(50).map(::Person)
    }


    override suspend fun count(): Long = connection.dbQuery {
        People.slice(People.id.count())
            .selectAll()
            .single()[People.id.count()]
    }
}