package dev.valvassori.rinha.domain.model

import dev.valvassori.rinha.database.entity.People
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Person(
    val id: String,
    @SerialName("apelido")
    val nick: String,
    @SerialName("nome")
    val name: String,
    @SerialName("nascimento")
    val birthDate: LocalDate,
    val stack: List<String>,
) {
    constructor(row: ResultRow) : this(
        id = row[People.id].value.toString(),
        nick = row[People.nick],
        name = row[People.name],
        birthDate = row[People.birthDate],
        stack = row[People.stack].toList(),
    )
}