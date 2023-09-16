package dev.valvassori.rinha.ext

import dev.valvassori.rinha.errors.UnprocessableEntityException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

suspend inline fun <reified T : Any> ApplicationCall.receiveOrUnprocessableEntity(): T = try {
    receive<T>()
} catch (error: Throwable) {
    throw UnprocessableEntityException.fromBodyParsing()
}