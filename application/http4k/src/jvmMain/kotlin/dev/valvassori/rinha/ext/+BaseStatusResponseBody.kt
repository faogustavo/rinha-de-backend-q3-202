package dev.valvassori.rinha.ext

import dev.valvassori.rinha.domain.reponse.BaseStatusResponseBody
import org.http4k.core.Body
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.BiDiBodyLens

private val bodyLensInstance: BiDiBodyLens<BaseStatusResponseBody> = Body.auto<BaseStatusResponseBody>().toLens()
val BaseStatusResponseBody.Companion.bodyLens get() = bodyLensInstance
