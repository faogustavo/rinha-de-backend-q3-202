package dev.valvassori.rinha.domain.reponse

import kotlinx.serialization.Serializable

@Serializable
data class BaseStatusResponseBody(
    val code: Int,
    val message: String,
) {
    // For extension purposes
    companion object {
        val serverUp = BaseStatusResponseBody(200, "Server is UP!")
        val internalServerError = BaseStatusResponseBody(500, "Internal Server Error")
    }
}