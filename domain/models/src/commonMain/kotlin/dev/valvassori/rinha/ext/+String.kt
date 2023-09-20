package dev.valvassori.rinha.ext

fun String.capitalizeFirstLetter(): String =
    this.replaceFirstChar { it.uppercase() }