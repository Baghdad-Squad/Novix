package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.converter.Converters
import org.junit.Assert.*
import org.junit.jupiter.api.Test

class ConvertersTest {
    @Test
    fun `fromStringToListString should split string into list`() {
        val input = "apple,banana,carrot"
        val result = Converters().fromStringToListString(input)
        assertEquals(listOf("apple", "banana", "carrot"), result)
    }

    @Test
    fun `StringlistToString should join list into string`() {
        val input = listOf("dog", "cat", "rabbit")
        val result = Converters().StringlistToString(input)
        assertEquals("dog,cat,rabbit", result)
    }

    @Test
    fun `fromStringToListLong should convert comma-separated numbers to list of Long`() {
        val input = "10,20,30"
        val result = Converters().fromStringToListLong(input)
        assertEquals(listOf(10L, 20L, 30L), result)
    }

    @Test
    fun `fromStringToListLong should return empty list for blank input`() {
        val input = ""
        val result = Converters().fromStringToListLong(input)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `longListToString should join list of Long into comma-separated string`() {
        val input = listOf(1L, 2L, 3L)
        val result = Converters().longListToString(input)
        assertEquals("1,2,3", result)
    }
}