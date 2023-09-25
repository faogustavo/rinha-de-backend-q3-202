package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.JDBCConnection
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.ext.asListFromDB
import dev.valvassori.rinha.ext.fromDB
import kotlinx.datetime.toJavaLocalDate
import java.util.UUID

internal class JDBCPersonDAOImpl internal constructor(private val connection: JDBCConnection) : PersonDAO {
    override suspend fun getById(id: String): Person? = connection.dbQuery {
        prepareStatement("SELECT id, name, nick, birth_date, stack FROM people WHERE id = ?")
            .apply { setObject(1, UUID.fromString(id)) }
            .executeQuery()
            .let { if (it.next()) Person.fromDB(it) else null }
    }

    override suspend fun create(person: Person): Unit = connection.dbQuery {
        prepareStatement("INSERT INTO people (id, name, nick, birth_date, stack, search) VALUES (?, ?, ?, ?, ?, ?)")
            .apply {
                setObject(1, UUID.fromString(person.id))
                setString(2, person.name)
                setString(3, person.nick)
                setObject(4, person.birthDate.toJavaLocalDate())
                setArray(5, createArrayOf("text", person.stack.toTypedArray()))
                setString(6, person.search)
            }
            .execute()
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery {
        prepareStatement("SELECT id, name, nick, birth_date, stack FROM people WHERE search LIKE ?")
            .apply { setString(1, "%${term.lowercase()}%") }
            .executeQuery()
            .let(Person::asListFromDB)
    }

    override suspend fun count(): Long = connection.dbQuery {
        prepareStatement("SELECT COUNT(*) FROM people")
            .executeQuery()
            .apply { next() }
            .getLong(1)
    }
}