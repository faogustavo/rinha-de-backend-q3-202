package dev.valvassori.rinha.routes

import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
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
            if (t.isNullOrEmpty()) {
                throw Exception("You must provide a query parameter named 't'")
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
            val id = call.parameters["id"]
                ?: throw Exception("You must provide the ID in the path")

            call.respond(
                Person(
                    id = id,
                    nick = "faogustavo",
                    name = "Gustavo Valvassori",
                    birthDate = "1995-08-07".toLocalDate(),
                    stack = listOf("Kotlin", "Java", "JS", "TS", "Python", "Go", "Rust")
                )
            )
        }

        post("/pessoas") {
            val newPerson = call.receive<NewPerson>()

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