package dev.valvassori.rinha.database.dao

import dev.valvassori.rinha.database.ExposedDBConnection
import dev.valvassori.rinha.database.entity.MySQLPeople
import dev.valvassori.rinha.database.entity.PGPeople
import dev.valvassori.rinha.database.model.SQLDialect
import dev.valvassori.rinha.datasource.PersonDAO
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.ext.fromDB
import dev.valvassori.rinha.helpers.JsonParser
import kotlinx.serialization.encodeToString
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

internal class ExposedPersonDAOImpl internal constructor(private val connection: ExposedDBConnection) : PersonDAO {
    override suspend fun getById(id: String): Person? = connection.dbQuery { dialect ->
        when (dialect) {
            SQLDialect.POSTGRES -> PGPeople.select { PGPeople.id eq UUID.fromString(id) }
            SQLDialect.MYSQL -> MySQLPeople.select { MySQLPeople.id eq id }
        }.limit(1).singleOrNull()?.let { Person.fromDB(it, dialect) }
    }

    override suspend fun create(person: Person) {
        connection.dbQuery { dialect ->
            when (dialect) {
                SQLDialect.POSTGRES -> PGPeople.insert {
                    it[id] = UUID.fromString(person.id)
                    it[nick] = person.nick
                    it[name] = person.name
                    it[birthDate] = person.birthDate
                    it[stack] = person.stack.toTypedArray()
                    it[search] = person.search
                }

                SQLDialect.MYSQL -> MySQLPeople.insert {
                    it[id] = person.id
                    it[nick] = person.nick
                    it[name] = person.name
                    it[birthDate] = person.birthDate
                    it[stack] = JsonParser.encodeToString(person.stack)
                    it[search] = person.search
                }
            }
        }
    }

    override suspend fun find(term: String): List<Person> = connection.dbQuery { dialect ->
        when (dialect) {
            SQLDialect.POSTGRES -> PGPeople.select { PGPeople.search like "%${term.lowercase()}%" }
            SQLDialect.MYSQL -> MySQLPeople.select { MySQLPeople.search match term.lowercase() }
        }.limit(50).map { Person.fromDB(it, dialect) }
    }

    override suspend fun count(): Long = connection.dbQuery { dialect ->
        when (dialect) {
            SQLDialect.POSTGRES -> PGPeople.slice(PGPeople.id.count())
                .selectAll()
                .single()[PGPeople.id.count()]

            SQLDialect.MYSQL -> MySQLPeople.slice(MySQLPeople.id.count())
                .selectAll()
                .single()[MySQLPeople.id.count()]
        }
    }
}