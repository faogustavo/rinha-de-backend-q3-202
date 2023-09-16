package dev.valvassori.rinha.errors

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import dev.valvassori.rinha.ext.capitalizeFirstLetter
import io.ktor.http.HttpStatusCode

abstract class APIException(
    val code: HttpStatusCode,
    override val message: String,
    override val cause: Throwable? = null,
) : Throwable(message, cause) {
    fun asResponse() = BaseStatusResponseBody(code.value, message)
}

// 400
class BadRequestException(message: String, cause: Throwable? = null) : APIException(
    code = HttpStatusCode.BadRequest,
    message = message,
    cause = cause,
)

// 404
class NotFoundException(resourceName: String = "Resource", cause: Throwable? = null) : APIException(
    code = HttpStatusCode.NotFound,
    message = "${resourceName.capitalizeFirstLetter()} not found",
    cause = cause,
)

// 422
class UnprocessableEntityException(message: String, cause: Throwable? = null) : APIException(
    code = HttpStatusCode.UnprocessableEntity,
    message = message,
    cause = cause,
) {
    companion object {
        fun fromBodyParsing() = UnprocessableEntityException("Invalid body")
    }
}

// 500
class InternalServerError(
    message: String = "Internal Server Error",
    cause: Throwable? = null,
) : APIException(HttpStatusCode.InternalServerError, message, cause) {
    constructor(cause: Throwable) : this("Internal Server Error", cause)
}