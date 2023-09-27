package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.JDBCConnection
import dev.valvassori.rinha.database.model.SQLDialect
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.ext.asListFromDB
import dev.valvassori.rinha.ext.fromDB
import dev.valvassori.rinha.helpers.JsonParser
import kotlinx.datetime.toJavaLocalDate
import kotlinx.serialization.encodeToString
import java.util.UUID

internal class JDBCPersonDAOImpl internal constructor(private val connection: JDBCConnection) : PersonDAO {
    override suspend fun getById(id: String): Person? = connection.dbQuery { dialect ->
        prepareStatement("SELECT id, name, nick, birth_date, stack FROM people WHERE id = ?")
            .apply {
                when (dialect) {
                    SQLDialect.POSTGRES -> setObject(1, UUID.fromString(id))
                    SQLDialect.MYSQL -> setString(1, id)
                }
            }
            .executeQuery()
            .let { if (it.next()) Person.fromDB(it, dialect) else null }
    }

    override suspend fun create(person: Person): Unit = connection.dbQuery { dialect ->
        prepareStatement("INSERT INTO people (id, name, nick, birth_date, stack, search) VALUES (?, ?, ?, ?, ?, ?)")
            .apply {
                setString(2, person.name)
                setString(3, person.nick)
                setObject(4, person.birthDate.toJavaLocalDate())
                setString(6, person.search)

                when (dialect) {
                    SQLDialect.POSTGRES -> {
                        setObject(1, UUID.fromString(person.id))
                        setArray(5, createArrayOf("text", person.stack.toTypedArray()))
                    }
                    SQLDialect.MYSQL -> {
                        setString(1, person.id)
                        setString(5, JsonParser.encodeToString(person.stack))
                    }
                }
            }
            .execute()
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery { dialect ->
        when (dialect) {
            SQLDialect.POSTGRES -> prepareStatement("SELECT id, name, nick, birth_date, stack FROM people WHERE search LIKE ?")
            SQLDialect.MYSQL -> prepareStatement("SELECT id, name, nick, birth_date, stack FROM people WHERE MATCH(search) AGAINST(?)")
        }.apply { setString(1, "%${term.lowercase()}%") }
            .executeQuery()
            .let { Person.asListFromDB(it, dialect) }
    }

    override suspend fun count(): Long = connection.dbQuery {
        prepareStatement("SELECT COUNT(*) FROM people")
            .executeQuery()
            .apply { next() }
            .getLong(1)
    }
}