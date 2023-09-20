package dev.valvassori.rinha.errors

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import dev.valvassori.rinha.ext.capitalizeFirstLetter

abstract class APIException(
    val code: Int,
    override val message: String,
    override val cause: Throwable? = null,
) : Throwable(message, cause) {
    fun asResponse() = BaseStatusResponseBody(code, message)
}

// 400
class BadRequestException(
    message: String,
    cause: Throwable? = null
) : APIException(400, message, cause)

// 404
class NotFoundException(
    resourceName: String = "Resource",
    cause: Throwable? = null
) : APIException(404, "${resourceName.capitalizeFirstLetter()} not found", cause)

// 422
class UnprocessableEntityException(
    message: String,
    cause: Throwable? = null
) : APIException(422, message, cause) {
    companion object {
        fun fromBodyParsing() = UnprocessableEntityException("Invalid body")
    }
}

// 500
class InternalServerError(
    message: String = "Internal Server Error",
    cause: Throwable? = null,
) : APIException(500, message, cause) {
    constructor(cause: Throwable) : this("Internal Server Error", cause)
}