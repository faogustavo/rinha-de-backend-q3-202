package dev.valvassori.rinha.routes

import dev.valvassori.rinha.ApplicationDependencies
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.errors.NotFoundException
import dev.valvassori.rinha.ext.receiveOrUnprocessableEntity
import dev.valvassori.rinha.validation.ErrorType
import dev.valvassori.rinha.validation.validateRequestInput
import dev.valvassori.rinha.validation.validateUUID
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.personRoutes() {
    routing {
        get("/pessoas") {
            val t = call.request.queryParameters["t"]
            validateRequestInput(!t.isNullOrEmpty(), errorType = ErrorType.BadRequest) {
                "You must provide a query parameter named 't'"
            }

            call.respond(ApplicationDependencies.repository.find(t))
        }

        get("/pessoas/{id}") {
            val id = validateUUID(call.parameters["id"]) {
                "You must provide a valid UUID for the 'id' path parameter"
            }

            when (val person = ApplicationDependencies.repository.getById(id.toString())) {
                null -> throw NotFoundException("Person")
                else -> call.respond(person)
            }
        }

        post("/pessoas") {
            val newPerson = call.receiveOrUnprocessableEntity<NewPerson>()
            val createdPerson = ApplicationDependencies.repository.create(newPerson)

            call.response.headers.append("Location", "/pessoas/${createdPerson.id}")
            call.respond(HttpStatusCode.Created, createdPerson)
        }

        get("/contagem-pessoas") {
            call.respond(ApplicationDependencies.repository.count())
        }
    }
}