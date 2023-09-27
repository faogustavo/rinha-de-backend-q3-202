package dev.valvassori.rinha.database.model

enum class SQLDialect {
    MYSQL, POSTGRES;

    companion object {
        fun fromDriverName(value: String): SQLDialect = when (value) {
            "org.postgresql.Driver" -> POSTGRES
            "com.mysql.cj.jdbc.Driver" -> MYSQL
            else -> throw IllegalArgumentException("Unknown driver: $value")
        }
    }
}