package dev.valvassori.rinha.domain.model

import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.helpers.randomUUID
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    val search: String by lazy {
        buildList {
            add(nick)
            add(name)
            addAll(stack)
        }.joinToString(" ").lowercase()
    }

    companion object {
        fun fromNewPerson(newPerson: NewPerson) = Person(
            id = randomUUID(),
            nick = newPerson.nick,
            name = newPerson.name,
            birthDate = newPerson.birthDate,
            stack = newPerson.stack.orEmpty(),
        )
    }
}