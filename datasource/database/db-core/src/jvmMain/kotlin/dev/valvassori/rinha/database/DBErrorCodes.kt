package dev.valvassori.rinha.database

import dev.valvassori.rinha.database.model.SQLDialect

object DBErrorCodes {
    object IntegrityViolation {
        fun uniqueViolation(dialect: SQLDialect) = when (dialect) {
            SQLDialect.POSTGRES -> "23505"
            SQLDialect.MYSQL -> "23000"
        }
    }
}