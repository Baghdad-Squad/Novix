package com.baghdad.localDatasource

import com.baghdad.localDatasource.roomDB.converter.Converters
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ConvertersTest {

    @Test
    fun `fromStringToListLong should convert comma-separated numbers when valid string provided`() {
        val input = "10,20,30"

        val result = Converters().fromStringToListLong(input)

        assertThat(result).isEqualTo(listOf(10L, 20L, 30L))
    }

    @Test
    fun `fromStringToListLong should return empty list when input is blank`() {
        val input = ""

        val result = Converters().fromStringToListLong(input)

        assertThat(result).isEmpty()
    }

    @Test
    fun `longListToString should join list into comma-separated string when list is provided`() {
        val input = listOf(1L, 2L, 3L)

        val result = Converters().longListToString(input)

        assertThat(result).isEqualTo("1,2,3")
    }

    @Test
    fun `longListToString should return empty string when list is empty`() {
        val input = emptyList<Long>()

        val result = Converters().longListToString(input)

        assertThat(result).isEmpty()
    }

}
