package dev.valvassori.rinha.reponses

import kotlinx.serialization.Serializable

@Serializable
data class BaseStatusResponseBody(
    val code: Int,
    val message: String,
)