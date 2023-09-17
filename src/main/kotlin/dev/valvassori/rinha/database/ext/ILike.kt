package dev.valvassori.rinha.database.ext

import org.jetbrains.exposed.sql.ComparisonOp
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryParameter

/**
 * Original code: https://github.com/JetBrains/Exposed/issues/622
 */

class InsensitiveLikeOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(
    expr1 = expr1,
    expr2 = expr2,
    opSign = "ILIKE",
)

infix fun <T : String?> ExpressionWithColumnType<T>.ilike(pattern: String): Op<Boolean> = InsensitiveLikeOp(
    expr1 = this,
    expr2 = QueryParameter(
        value = pattern,
        sqlType = columnType,
    ),
)