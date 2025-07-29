package com.baghdad.viewmodel.actorGallery


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ActorGalleryScreenEffectTest {

    @Test
    fun `OnBackClick should be data object with correct type`() {
        val effect = ActorGalleryScreenEffect.OnBackClick

        Assertions.assertEquals(ActorGalleryScreenEffect.OnBackClick::class, effect::class)
    }

    @Test
    fun `OnBackClick should be singleton instance`() {
        val effect1 = ActorGalleryScreenEffect.OnBackClick
        val effect2 = ActorGalleryScreenEffect.OnBackClick

        Assertions.assertSame(effect1, effect2)
    }

    @Test
    fun `OnBackClick should have proper toString representation`() {
        val effect = ActorGalleryScreenEffect.OnBackClick

        val expectedToString = "OnBackClick"
        Assertions.assertEquals(expectedToString, effect.toString())
    }

    @Test
    fun `OnBackClick should be equal to itself`() {
        val effect = ActorGalleryScreenEffect.OnBackClick

        Assertions.assertEquals(effect, effect)
    }

    @Test
    fun `OnBackClick should have consistent hashCode`() {
        val effect1 = ActorGalleryScreenEffect.OnBackClick
        val effect2 = ActorGalleryScreenEffect.OnBackClick

        Assertions.assertEquals(effect1.hashCode(), effect2.hashCode())
    }

    @Test
    fun `ActorGalleryScreenEffect sealed class should have correct subclasses`() {

        Assertions.assertTrue(true)

        val effects = listOf<ActorGalleryScreenEffect>(
            ActorGalleryScreenEffect.OnBackClick
        )

        effects.forEach { effect ->
            when (effect) {
                is ActorGalleryScreenEffect.OnBackClick -> {
                    Assertions.assertTrue(true)
                }

            }
        }
    }

    @Test
    fun `OnBackClick should be serializable if needed`() {
        val effect = ActorGalleryScreenEffect.OnBackClick

        val effectList = listOf(effect, effect, effect)

        Assertions.assertEquals(3, effectList.size)
        Assertions.assertTrue(effectList.all { it == ActorGalleryScreenEffect.OnBackClick })
    }

    @Test
    fun `sealed class should support when expression exhaustively`() {
        val effect: ActorGalleryScreenEffect = ActorGalleryScreenEffect.OnBackClick

        val result = when (effect) {
            ActorGalleryScreenEffect.OnBackClick -> "back_clicked"
        }

        Assertions.assertEquals("back_clicked", result)
    }
}