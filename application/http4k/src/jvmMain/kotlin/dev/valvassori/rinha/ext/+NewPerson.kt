package dev.valvassori.rinha.ext

import dev.valvassori.rinha.domain.request.NewPerson
import org.http4k.core.Body
import org.http4k.format.KotlinxSerialization.auto

private val bodyLensInstance = Body.auto<NewPerson>().toLens()
val NewPerson.Companion.bodyLens get() = bodyLensInstance.withErrorHandling()
