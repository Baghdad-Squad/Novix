package com.baghdad.local_datasource

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FakeClassTest {
    @Test
    fun testHello() {
        val result = FakeClass().hello()
        assertEquals("Hello, world!", result)
    }
}