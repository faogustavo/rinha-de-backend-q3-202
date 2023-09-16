package dev.valvassori.rinha.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
)