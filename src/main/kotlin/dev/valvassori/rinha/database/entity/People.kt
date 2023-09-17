package dev.valvassori.rinha.database.entity

import dev.valvassori.rinha.database.ext.textArray
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object People : UUIDTable("people") {
    val nick = varchar("nick", 32).uniqueIndex()
    val name = varchar("name", 100)
    val birthDate = date("birth_date")
    val stack = textArray("stack")
    val search = text("search")
}