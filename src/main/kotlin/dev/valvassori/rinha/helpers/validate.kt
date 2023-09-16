package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.errors.UnprocessableEntityException
import java.util.UUID
import kotlin.contracts.contract

inline fun validateRequestInput(
    condition: Boolean,
    lazyMessage: () -> Any
) {
    contract {
        returns() implies condition
    }

    if (!condition) {
        val message = lazyMessage()
        throw UnprocessableEntityException(message.toString())
    }
}

inline fun validateUUID(
    value: String?,
    lazyMessage: () -> Any
): UUID = try {
    require(value != null)
    UUID.fromString(value)
} catch (e: Throwable) {
    val message = lazyMessage()
    throw UnprocessableEntityException(message.toString(), e)
}