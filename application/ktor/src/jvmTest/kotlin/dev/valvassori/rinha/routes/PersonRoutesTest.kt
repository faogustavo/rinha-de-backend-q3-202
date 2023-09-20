package dev.valvassori.rinha.routes

import dev.valvassori.rinha.testApplication
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonRoutesTest {

    // Get by term routes
    @Test
    fun getWithTerm_withoutQueryParameter_returnUnprocessableEntity() = testPersonRoute {
        val response = client.get("/pessoas")
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun getWithTerm_withQueryParameter_returnOK() = testPersonRoute {
        val response = client.get("/pessoas?t=test")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    // Get by id
    @Test
    fun getById_withInvalidUUID_returnUnprocessableEntity() = testPersonRoute {
        val response = client.get("/pessoas/invalid-uuid")
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun getById_withValidUUID_returnOK() = testPersonRoute {
        val response = client.get("/pessoas/8a3f8a3f-8a3f-8a3f-8a3f-8a3f8a3f8a3f")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    // Create new person
    @Test
    fun create_withoutBody_returnUnprocessableEntity() = testPersonRoute {
        val response = client.post("/pessoas")
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun create_withInvalidBody_returnUnprocessableEntity() = testPersonRoute {
        val response = client.post("/pessoas") {
            setBody("{\"foo\": \"bar\"}")
        }
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    private fun testPersonRoute(block: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            application { personRoutes() }
            block()
        }
}