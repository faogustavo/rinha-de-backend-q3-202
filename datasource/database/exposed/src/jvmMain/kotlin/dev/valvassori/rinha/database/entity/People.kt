package dev.valvassori.rinha.database.entity

import dev.valvassori.rinha.database.ext.textArray
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object PGPeople : UUIDTable("people") {
    val nick = varchar("nick", 32).uniqueIndex()
    val name = varchar("name", 100)
    val birthDate = date("birth_date")
    val stack = textArray("stack")
    val search = text("search")
}
object MySQLPeople : IdTable<String>("people") {
    override val id = varchar("id", 36).entityId()
    val nick = varchar("nick", 32).uniqueIndex()
    val name = varchar("name", 100)
    val birthDate = date("birth_date")
    val stack = text("stack")
    val search = text("search")
}