package dev.valvassori.rinha

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import dev.valvassori.rinha.ext.bodyLens
import dev.valvassori.rinha.helpers.ErrorHandler
import dev.valvassori.rinha.routes.PersonRoutes
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    routes(HomeRoutes, PersonRoutes)
        .withFilter(ErrorHandler(CoreApplicationEnv.returnErrorBody))
        .asServer(Netty(CoreApplicationEnv.port))
        .start()
        .block()
}

private val HomeRoutes: RoutingHttpHandler = routes(
    "/" bind Method.GET to { req ->
        BaseStatusResponseBody.bodyLens(
            BaseStatusResponseBody.serverUp,
            Response(Status.OK)
        )
    }
)