package dev.valvassori.rinha.helpers

import kotlinx.serialization.json.Json

val JsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
}