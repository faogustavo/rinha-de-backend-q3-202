package dev.valvassori.rinha.domain.reponse

import kotlinx.serialization.Serializable

@Serializable
data class BaseStatusResponseBody(
    val code: Int,
    val message: String,
)