package dev.valvassori.rinha.ext

import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.PathMethod

infix fun PathMethod.toSuspend(action: suspend (request: Request) -> Response) = to {
    runBlocking { action(it) }
}