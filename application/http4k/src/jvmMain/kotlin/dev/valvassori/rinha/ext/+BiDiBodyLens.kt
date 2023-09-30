package dev.valvassori.rinha.ext

import dev.valvassori.rinha.errors.UnprocessableEntityException
import org.http4k.lens.BiDiBodyLens

fun <T> BiDiBodyLens<T>.withErrorHandling(): BiDiBodyLens<T> = BiDiBodyLens(
    metas,
    contentType,
    { httpMessage ->
        try {
            this(httpMessage)
        } catch (e: Throwable) {
            throw UnprocessableEntityException.fromBodyParsing()
        }
    },
    { newPerson, httpMessage -> this(newPerson, httpMessage) }
)