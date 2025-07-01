package com.baghdad.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FakeClassTest {
    @Test
    fun testHello() {
        val result = FakeClass().hello()
        assertEquals("Hello, world!", result)
    }
}