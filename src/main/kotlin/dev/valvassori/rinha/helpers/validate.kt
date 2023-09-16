package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.errors.UnprocessableEntityException
import java.util.UUID
import kotlin.contracts.contract

inline fun validateRequestInput(
    value: Boolean,
    lazyMessage: () -> Any
) {
    contract {
        returns() implies value
    }

    if (!value) {
        val message = lazyMessage()
        throw UnprocessableEntityException(message.toString())
    }
}

inline fun validateUUID(
    value: String?,
    lazyMessage: () -> Any
): UUID = try {
    UUID.fromString(value)
} catch (e: Throwable) {
    val message = lazyMessage()
    throw UnprocessableEntityException(message.toString(), e)
}