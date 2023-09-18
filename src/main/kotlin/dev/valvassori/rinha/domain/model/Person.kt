package dev.valvassori.rinha.domain.model

import dev.valvassori.rinha.database.entity.People
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.serializer.UUIDSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

@Serializable
data class Person(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    @SerialName("apelido")
    val nick: String,
    @SerialName("nome")
    val name: String,
    @SerialName("nascimento")
    val birthDate: LocalDate,
    val stack: List<String>,
) {
    constructor(row: ResultRow) : this(
        id = row[People.id].value,
        nick = row[People.nick],
        name = row[People.name],
        birthDate = row[People.birthDate],
        stack = row[People.stack].toList(),
    )

    constructor(newPerson: NewPerson) : this(
        id = UUID.randomUUID(),
        nick = newPerson.nick,
        name = newPerson.name,
        birthDate = newPerson.birthDate,
        stack = newPerson.stack.orEmpty(),
    )
}