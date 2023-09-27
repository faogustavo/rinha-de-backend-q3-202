package dev.valvassori.rinha.ext

import com.zaxxer.hikari.HikariDataSource
import dev.valvassori.rinha.database.model.SQLDialect

fun HikariDataSource.dialect(): SQLDialect =
    SQLDialect.fromDriverName(driverClassName)