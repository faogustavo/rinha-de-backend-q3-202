package dev.valvassori.rinha

import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication as ktorTestApplication

fun testApplication(block: suspend ApplicationTestBuilder.() -> Unit) = ktorTestApplication {
    application { installCoreDependencies() }
    block()
}