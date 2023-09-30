package dev.valvassori.rinha.ext

import dev.valvassori.rinha.domain.model.Person
import org.http4k.core.Body
import org.http4k.format.KotlinxSerialization.auto

private val bodyLensInstance = Body.auto<Person>().toLens()
private val listBodyLensInstance = Body.auto<List<Person>>().toLens()
val Person.Companion.bodyLens get() = bodyLensInstance
val Person.Companion.listBodyLens get() = listBodyLensInstance
