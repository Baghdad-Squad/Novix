package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiEffect
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MainEffectTest {

    @Test
    fun `should be a subclass of BaseUiEffect`() {
        // Given
        val effect = MainEffect()

        // Then
        assertThat(effect).isInstanceOf(BaseUiEffect::class.java)
    }

    @Test
    fun `should have meaningful toString representation`() {
        // Given
        val effect = MainEffect()

        // Then
        assertThat(effect.toString()).contains("MainEffect")
    }

    @Test
    fun `should be usable in sealed class hierarchies`() {
        // Given
        val effect: BaseUiEffect = MainEffect()

        // Then
        assertThat(effect).isInstanceOf(MainEffect::class.java)
    }

    @Test
    fun `should be data class if it needs value equality`() {
        // Given
        val effectClass = MainEffect::class

        // Then
        assertThat(effectClass.isData).isFalse()
    }

}