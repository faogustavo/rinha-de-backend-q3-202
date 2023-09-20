package dev.valvassori.rinha.ext

import dev.valvassori.rinha.database.entity.People
import dev.valvassori.rinha.domain.model.Person
import org.jetbrains.exposed.sql.ResultRow

fun Person.Companion.fromDB(row: ResultRow) = Person(
    id = row[People.id].value.toString(),
    nick = row[People.nick],
    name = row[People.name],
    birthDate = row[People.birthDate],
    stack = row[People.stack].toList(),
)