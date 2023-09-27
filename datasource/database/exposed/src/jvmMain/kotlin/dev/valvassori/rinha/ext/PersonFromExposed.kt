package dev.valvassori.rinha.ext

import dev.valvassori.rinha.database.entity.MySQLPeople
import dev.valvassori.rinha.database.entity.PGPeople
import dev.valvassori.rinha.database.model.SQLDialect
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.helpers.JsonParser
import org.jetbrains.exposed.sql.ResultRow

fun Person.Companion.fromDB(row: ResultRow, dialect: SQLDialect) = when (dialect) {
    SQLDialect.POSTGRES -> Person(
        id = row[PGPeople.id].value.toString(),
        nick = row[PGPeople.nick],
        name = row[PGPeople.name],
        birthDate = row[PGPeople.birthDate],
        stack = row[PGPeople.stack].toList(),
    )
    SQLDialect.MYSQL -> Person(
        id = row[MySQLPeople.id].value,
        nick = row[MySQLPeople.nick],
        name = row[MySQLPeople.name],
        birthDate = row[MySQLPeople.birthDate],
        stack = JsonParser.decodeFromString(row[MySQLPeople.stack]),
    )
}