package com.baghdad.viewmodel.dummyData

object DummyDataFactory {
    fun createMockGallery() = (1..15).map { index ->
        "/gallery_image_$index.jpg"
    }
}