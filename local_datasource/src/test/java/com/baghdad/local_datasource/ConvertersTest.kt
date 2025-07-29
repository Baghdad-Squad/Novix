package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.converter.Converters
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConvertersTest {
    @Test
    fun `fromStringToListString should split string into list`() {
        // Given
        val input = "apple,banana,carrot"

        // When
        val result = Converters().fromStringToListString(input)

        // Then
        Assertions.assertEquals(listOf("apple", "banana", "carrot"), result)
    }

    @Test
    fun `StringlistToString should join list into string`() {
        // Given
        val input = listOf("dog", "cat", "rabbit")

        // When
        val result = Converters().StringlistToString(input)

        // Then
        Assertions.assertEquals("dog,cat,rabbit", result)
    }

    @Test
    fun `fromStringToListLong should convert comma-separated numbers to list of Long`() {
        // Given
        val input = "10,20,30"

        // When
        val result = Converters().fromStringToListLong(input)

        // Then
        Assertions.assertEquals(listOf(10L, 20L, 30L), result)
    }

    @Test
    fun `fromStringToListLong should return empty list for blank input`() {
        // Given
        val input = ""

        // When
        val result = Converters().fromStringToListLong(input)

        // Then
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `longListToString should join list of Long into comma-separated string`() {
        // Given
        val input = listOf(1L, 2L, 3L)

        // When
        val result = Converters().longListToString(input)

        // Then
        Assertions.assertEquals("1,2,3", result)
    }
}