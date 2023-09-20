package dev.valvassori.rinha.domain.request

import dev.valvassori.rinha.errors.UnprocessableEntityException
import dev.valvassori.rinha.helpers.JsonParser
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertIs

class NewPersonTests {

    @Test
    fun withValidInput_shouldWork() {
        fun runTest(json: String) {
            JsonParser.decodeFromString<NewPerson>(json)
            // If it reaches this point, it means that the test passed
        }

        setOf(
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": []
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": null
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07"
                }
            """.trimIndent(),
        ).forEach(::runTest)
    }

    @Test
    fun withMissingRequiredAttributes_shouldThrowException() {
        fun runTest(json: String) {
            assertFails { JsonParser.decodeFromString<NewPerson>(json) }
        }

        setOf(
            """
                {
                    "nome": "Gustavo",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "apelido": "faogustavo",
                    "nome": "Gustavo",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
        ).forEach(::runTest)
    }

    @Test
    fun withInvalidDates_shouldThrowException() {
        fun runTest(json: String) {
            assertFails { JsonParser.decodeFromString<NewPerson>(json) }
        }

        setOf(
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "¯\_(ツ)_/¯",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-072",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-32",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "19925-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
            """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-13-32",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
            """.trimIndent(),
        ).forEach(::runTest)
    }

    @Test
    fun withLongerNick_shouldFail() {
        val error = assertFails {
            JsonParser.decodeFromString<NewPerson>(
                """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo with a longer nick to pass the 32 chars limit",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
                """.trimIndent(),
            )
        }
        assertIs<UnprocessableEntityException>(error)
    }

    @Test
    fun withLongerName_shouldFail() {
        val error = assertFails {
            JsonParser.decodeFromString<NewPerson>(
                """
                {
                    "nome": "Gustavo ${"Long ".repeat(25)} Name",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin",
                        "Java"
                    ]
                }
                """.trimIndent(),
            )
        }
        assertIs<UnprocessableEntityException>(error)
    }

    @Test
    fun withLongerStackName_shouldFail() {
        val error = assertFails {
            JsonParser.decodeFromString<NewPerson>(
                """
                {
                    "nome": "Gustavo",
                    "apelido": "faogustavo",
                    "nascimento": "1995-08-07",
                    "stack": [
                        "Kotlin with a lot of extra chars to pass the 32 chars limit"
                    ]
                }
                """.trimIndent(),
            )
        }
        assertIs<UnprocessableEntityException>(error)
    }
}