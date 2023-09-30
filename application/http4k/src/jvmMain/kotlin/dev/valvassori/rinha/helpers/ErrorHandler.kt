package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import dev.valvassori.rinha.errors.APIException
import dev.valvassori.rinha.errors.InternalServerError
import dev.valvassori.rinha.ext.bodyLens
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.filter.ServerFilters

val ErrorHandler = { returnErrorBody: Boolean ->
    ServerFilters.CatchAll { failure ->
        when (failure) {
            is APIException -> {
                if (failure is InternalServerError) {
                    System.err.println("[Unhandled exception] ${failure.stackTraceToString()}")
                }

                if (returnErrorBody) {
                    BaseStatusResponseBody.bodyLens(
                        failure.asResponse(),
                        Response(Status.fromCode(failure.code) ?: Status.INTERNAL_SERVER_ERROR)
                    )
                } else {
                    Response(Status.fromCode(failure.code) ?: Status.INTERNAL_SERVER_ERROR)
                }
            }

            else -> {
                System.err.println("[Unhandled exception] ${failure.stackTraceToString()}")
                if (returnErrorBody) {
                    BaseStatusResponseBody.bodyLens(
                        BaseStatusResponseBody.internalServerError,
                        Response(Status.INTERNAL_SERVER_ERROR)
                    )
                } else {
                    Response(Status.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }
}