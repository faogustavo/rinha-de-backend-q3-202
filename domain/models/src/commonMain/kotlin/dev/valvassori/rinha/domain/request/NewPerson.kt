package dev.valvassori.rinha.domain.request

import dev.valvassori.rinha.validation.validateRequestInput
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
) {
    // For extension purposes
    companion object;
    init {
        validateRequestInput(nick.length <= 32) {
            "Nick must be at most 32 characters long"
        }

        validateRequestInput(name.length <= 100) {
            "Name must be at most 100 characters long"
        }

        stack?.forEach { tech ->
            validateRequestInput(tech.length <= 32) {
                "All items from the stack must be at most 32 characters long"
            }
        }
    }
}
