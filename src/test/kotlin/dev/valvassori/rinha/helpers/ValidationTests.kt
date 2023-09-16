package dev.valvassori.rinha.helpers

import dev.valvassori.rinha.errors.UnprocessableEntityException
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class ValidationTests {

    @Test
    fun validateRequestInput_withValidCondition_shouldNotThrow() {
        validateRequestInput((2 + 2) == 4) { "Invalid value" }
    }

    @Test
    fun validateRequestInput_withInvalidCondition_shouldThrowUnprocessableEntityException() {
        assertThrows<UnprocessableEntityException> {
            validateRequestInput((2 + 2) == 5) { "Invalid value" }
        }
    }

    @Test
    fun validateUUID_withValidUUID_shouldNotThrow() {
        val validValue = UUID.randomUUID().toString()
        validateUUID(validValue) { "Invalid UUID" }
    }

    @Test
    fun validateUUID_withInvalidUUID_shouldThrowUnprocessableEntityException() {
        val invalidValue = "Test"
        assertThrows<UnprocessableEntityException> {
            validateUUID(invalidValue) { "Invalid UUID" }
        }
    }
}