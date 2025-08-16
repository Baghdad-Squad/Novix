package com.baghdad.islamic_image_loader.cache

object BlurStateCache {
    private val cache = mutableMapOf<String, Boolean>()

    fun getBlurState(key: String): Boolean? = cache[key]

    fun setBlurState(
        key: String,
        isBlurred: Boolean,
    ) {
        cache[key] = isBlurred
    }
}
