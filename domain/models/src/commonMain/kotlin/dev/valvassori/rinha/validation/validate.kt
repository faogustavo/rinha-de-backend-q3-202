package dev.valvassori.rinha.validation

import dev.valvassori.rinha.errors.BadRequestException
import dev.valvassori.rinha.errors.UnprocessableEntityException
import java.util.UUID
import kotlin.contracts.contract

enum class ErrorType {
    UnprocessableEntity,
    BadRequest,
}

inline fun validateRequestInput(
    condition: Boolean,
    errorType: ErrorType = ErrorType.UnprocessableEntity,
    lazyMessage: () -> Any
) {
    contract {
        returns() implies condition
    }

    if (!condition) {
        val message = lazyMessage()
        when (errorType) {
            ErrorType.UnprocessableEntity -> throw UnprocessableEntityException(message.toString())
            ErrorType.BadRequest -> throw BadRequestException(message.toString())
        }
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
    throw BadRequestException(message.toString(), e)
}