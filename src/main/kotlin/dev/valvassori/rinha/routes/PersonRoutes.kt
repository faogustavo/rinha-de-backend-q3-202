package dev.valvassori.rinha.routes

import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.ext.receiveOrUnprocessableEntity
import dev.valvassori.rinha.helpers.validateRequestInput
import dev.valvassori.rinha.helpers.validateUUID
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.datetime.toLocalDate
import java.util.UUID

fun Application.personRoutes() {
    routing {
        get("/pessoas") {
            val t = call.request.queryParameters["t"]
            validateRequestInput(!t.isNullOrEmpty()) {
                "You must provide a query parameter named 't'"
            }

            call.respond(
                listOf(
                    Person(
                        id = UUID.randomUUID().toString(),
                        nick = "faogustavo",
                        name = "Gustavo Valvassori",
                        birthDate = "1995-08-07".toLocalDate(),
                        stack = listOf("Kotlin", "Java", "JS", "TS", "Python", "Go", "Rust")
                    )
                )
            )
        }

        get("/pessoas/{id}") {
            val id = validateUUID(call.parameters["id"]) {
                "You must provide a valid UUID for the 'id' path parameter"
            }

            call.respond(
                Person(
                    id = id.toString(),
                    nick = "faogustavo",
                    name = "Gustavo Valvassori",
                    birthDate = "1995-08-07".toLocalDate(),
                    stack = listOf("Kotlin", "Java", "JS", "TS", "Python", "Go", "Rust")
                )
            )
        }

        post("/pessoas") {
            val newPerson = call.receiveOrUnprocessableEntity<NewPerson>()

            call.respond(
                listOf(
                    Person(
                        id = UUID.randomUUID().toString(),
                        nick = newPerson.nick,
                        name = newPerson.name,
                        birthDate = newPerson.birthDate,
                        stack = newPerson.stack.orEmpty()
                    )
                )
            )
        }

        get("/contagem-pessoas") {
            call.respond(0)
        }
    }
}