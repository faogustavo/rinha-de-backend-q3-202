package dev.valvassori.rinha

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import dev.valvassori.rinha.errors.APIException
import dev.valvassori.rinha.errors.InternalServerError
import dev.valvassori.rinha.helpers.Env
import dev.valvassori.rinha.helpers.JsonParser
import dev.valvassori.rinha.routes.personRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.tomcat.Tomcat
import org.slf4j.event.Level

fun main() {
    embeddedServer(
        factory = when (Env.ENGINE) {
            Env.Engine.CIO -> CIO
            Env.Engine.Jetty -> Jetty
            Env.Engine.Netty -> Netty
            Env.Engine.Tomcat -> Tomcat
        },
        port = 8080,
    ) { init() }.start(wait = true)
}

private fun Application.init() {
    // Deps
    installCoreDependencies()

    // Routes
    personRoutes()
}

fun Application.installCoreDependencies() {
    install(ContentNegotiation) {
        json(JsonParser)
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is APIException -> {
                    if (cause is InternalServerError) {
                        call.application.log.error("Unhandled exception", cause)
                    }

                    call.respond(cause.code, cause.asResponse())
                }

                else -> {
                    call.application.log.error("Unhandled exception", cause)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        BaseStatusResponseBody(500, "Internal Server Error")
                    )
                }
            }
        }
    }

    routing {
        get("/") {
            call.respond(
                BaseStatusResponseBody(
                    code = 200,
                    message = "Server is UP!"
                )
            )
        }
    }
}