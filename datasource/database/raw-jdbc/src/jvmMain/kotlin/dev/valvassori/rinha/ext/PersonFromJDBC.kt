package dev.valvassori.rinha.ext

import dev.valvassori.rinha.domain.model.Person
import kotlinx.datetime.toKotlinLocalDate
import java.sql.ResultSet
import java.util.UUID

fun Person.Companion.asListFromDB(result: ResultSet) = buildList {
    while (result.next()) {
        Person.fromDB(result).let(::add)
    }
}

fun Person.Companion.fromDB(result: ResultSet) = Person(
    id = result.getObject("id", UUID::class.java).toString(),
    nick = result.getString("nick"),
    name = result.getString("name"),
    birthDate = result.getDate("birth_date").toLocalDate().toKotlinLocalDate(),
    stack = result.getArray("stack")
        .let { (it.array as Array<String>).toList() }
)