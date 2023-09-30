package dev.valvassori.rinha.routes

import dev.valvassori.rinha.ApplicationDependencies
import dev.valvassori.rinha.domain.model.Person
import dev.valvassori.rinha.domain.request.NewPerson
import dev.valvassori.rinha.errors.NotFoundException
import dev.valvassori.rinha.ext.bodyLens
import dev.valvassori.rinha.ext.listBodyLens
import dev.valvassori.rinha.ext.toSuspend
import dev.valvassori.rinha.validation.ErrorType
import dev.valvassori.rinha.validation.validateRequestInput
import dev.valvassori.rinha.validation.validateUUID
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

val PersonRoutes: RoutingHttpHandler = routes(
    "/pessoas" bind Method.GET toSuspend { req ->
        val t = req.query("t")
        validateRequestInput(!t.isNullOrEmpty(), errorType = ErrorType.BadRequest) {
            "You must provide a query parameter named 't'"
        }

        Person.listBodyLens(
            ApplicationDependencies.repository.find(t),
            Response(Status.OK)
        )
    },
    "/pessoas/{id}" bind Method.GET toSuspend { req ->
        val id = validateUUID(req.path("id")) {
            "You must provide a valid UUID for the 'id' path parameter"
        }

        when (val person = ApplicationDependencies.repository.getById(id.toString())) {
            null -> throw NotFoundException("Person")
            else -> Person.bodyLens(person, Response(Status.OK))
        }
    },
    "/pessoas" bind Method.POST toSuspend { req ->
        val newPerson = NewPerson.bodyLens(req)
        val createdPerson = ApplicationDependencies.repository.create(newPerson)

        Person.bodyLens(createdPerson, Response(Status.CREATED))
            .header("Location", "/pessoas/${createdPerson.id}")
    },
    "contagem-pessoas" bind Method.GET toSuspend { req ->
        Response(Status.OK)
            .body(ApplicationDependencies.repository.count().toString())
    }
)