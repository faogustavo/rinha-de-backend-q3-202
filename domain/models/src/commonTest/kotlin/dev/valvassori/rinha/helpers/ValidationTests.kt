package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.errors.BadRequestException
import dev.valvassori.rinha.errors.UnprocessableEntityException
import dev.valvassori.rinha.validation.validateRequestInput
import dev.valvassori.rinha.validation.validateUUID
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertIs

class ValidationTests {

    @Test
    fun validateRequestInput_withValidCondition_shouldNotThrow() {
        validateRequestInput((2 + 2) == 4) { "Invalid value" }
    }

    @Test
    fun validateRequestInput_withInvalidCondition_shouldThrowUnprocessableEntityException() {
        val error = assertFails {
            validateRequestInput((2 + 2) == 5) { "Invalid value" }
        }
        assertIs<UnprocessableEntityException>(error)
    }

    @Test
    fun validateUUID_withValidUUID_shouldNotThrow() {
        val validValue = UUID.randomUUID().toString()
        validateUUID(validValue) { "Invalid UUID" }
    }

    @Test
    fun validateUUID_withInvalidUUID_shouldThrowUnprocessableEntityException() {
        val invalidValue = "Test"
        val error = assertFails {
            validateUUID(invalidValue) { "Invalid UUID" }
        }

        assertIs<BadRequestException>(error)
    }
}