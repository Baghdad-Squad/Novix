package com.baghdad.islamic_image_loader.model

enum class ContentRestrictionTypes(val thresholds: Float) {
    STRICT(0.4f),
    MODERATE(0.65f),
    NONE(1f)
}