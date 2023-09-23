package dev.valvassori.rinha.routes

import dev.valvassori.rinha.Env
import dev.valvassori.rinha.cache.RedisCacheStorage
import dev.valvassori.rinha.cache.datasource.NoopPersonCache
import dev.valvassori.rinha.database.DBConnection
import dev.valvassori.rinha.datasource.PersonCache
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.errors.NotFoundException
import dev.valvassori.rinha.ext.receiveOrUnprocessableEntity
import dev.valvassori.rinha.factory.PersonRepositoryFactory
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

private val cache: PersonCache by lazy {
    if (Env.getBoolean("DISABLE_CACHE", false)) {
        NoopPersonCache
    } else {
        RedisCacheStorage.shared.personCache
    }
}

private val repository by lazy {
    PersonRepositoryFactory.newInstance(
        dao = DBConnection.shared.personDAO,
        cache = cache,
    )
}

fun Application.personRoutes() {
    routing {
        get("/pessoas") {
            val t = call.request.queryParameters["t"]
            validateRequestInput(!t.isNullOrEmpty(), errorType = ErrorType.BadRequest) {
                "You must provide a query parameter named 't'"
            }

            call.respond(repository.find(t))
        }

        get("/pessoas/{id}") {
            val id = validateUUID(call.parameters["id"]) {
                "You must provide a valid UUID for the 'id' path parameter"
            }

            when (val person = repository.getById(id.toString())) {
                null -> throw NotFoundException("Person")
                else -> call.respond(person)
            }
        }

        post("/pessoas") {
            val newPerson = call.receiveOrUnprocessableEntity<NewPerson>()
            val createdPerson = repository.create(newPerson)

            call.response.headers.append("Location", "/pessoas/${createdPerson.id}")
            call.respond(HttpStatusCode.Created, createdPerson)
        }

        get("/contagem-pessoas") {
            call.respond(repository.count())
        }
    }
}