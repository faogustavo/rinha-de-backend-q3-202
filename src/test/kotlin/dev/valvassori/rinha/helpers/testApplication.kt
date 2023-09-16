package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.installCoreDependencies
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication as ktorTestApplication

fun testApplication(block: suspend ApplicationTestBuilder.() -> Unit) = ktorTestApplication {
    this.application {
        installCoreDependencies()
    }

    block()
}