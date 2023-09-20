package dev.valvassori.rinha.helpers

actual fun randomUUID(): String =
    java.util.UUID.randomUUID().toString()