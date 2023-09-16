package dev.valvassori.rinha

import dev.valvassori.rinha.helpers.JsonParser
import dev.valvassori.rinha.reponses.BaseStatusResponseBody
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.slf4j.event.Level

fun main() {
    embeddedServer(CIO, port = 8080) {
        installCoreDependencies()
    }.start(wait = true)
}

fun Application.installCoreDependencies() {
    install(ContentNegotiation) {
        json(JsonParser)
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
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