package dev.valvassori.rinha.ext

import dev.valvassori.rinha.database.model.SQLDialect
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.helpers.JsonParser
import kotlinx.datetime.toKotlinLocalDate
import java.sql.ResultSet
import java.util.UUID

fun Person.Companion.asListFromDB(result: ResultSet, dialect: SQLDialect) = buildList {
    while (result.next()) {
        Person.fromDB(result, dialect).let(::add)
    }
}

fun Person.Companion.fromDB(result: ResultSet, dialect: SQLDialect) = Person(
    id = when (dialect) {
        SQLDialect.POSTGRES -> result.getObject("id", UUID::class.java).toString()
        else -> result.getString("id")
    },
    nick = result.getString("nick"),
    name = result.getString("name"),
    birthDate = result.getDate("birth_date").toLocalDate().toKotlinLocalDate(),
    stack = when (dialect) {
        SQLDialect.POSTGRES -> result.getArray("stack").let { (it.array as Array<String>).toList() }
        else -> JsonParser.decodeFromString(result.getString("stack"))
    }
)