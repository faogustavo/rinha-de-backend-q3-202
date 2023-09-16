package dev.valvassori.rinha.domain.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NewPerson(
    @SerialName("apelido")
    val nick: String,
    @SerialName("nome")
    val name: String,
    @SerialName("nascimento")
    val birthDate: LocalDate,
    val stack: List<String>? = null,
)
