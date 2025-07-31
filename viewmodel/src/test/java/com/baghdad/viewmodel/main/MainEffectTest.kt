package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiEffect
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MainEffectTest {

    @Test
    fun `MainEffect should inherit from BaseUiEffect when instantiated`() {
        // Given
        val effect = MainEffect()

        // Then
        assertThat(effect).isInstanceOf(BaseUiEffect::class.java)
    }

    @Test
    fun `toString should include class name when called on MainEffect`() {
        // Given
        val effect = MainEffect()

        // Then
        assertThat(effect.toString()).contains("MainEffect")
    }

    @Test
    fun `MainEffect should be assignable to BaseUiEffect when used in sealed hierarchy`() {
        // Given
        val effect: BaseUiEffect = MainEffect()

        // Then
        assertThat(effect).isInstanceOf(MainEffect::class.java)
    }

    @Test
    fun `MainEffect should not be a data class when value equality is not needed`() {
        // Given
        val effectClass = MainEffect::class

        // Then
        assertThat(effectClass.isData).isFalse()
    }
}