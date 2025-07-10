package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.entity.media.Media
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetRecentlyViewedUseCaseTest {
    private val getRecentlyViewedUseCase = GetRecentlyViewedUseCase()

    @Test
    fun dummyTest() = runTest {
        val result = getRecentlyViewedUseCase().first()
        Truth.assertThat(result).isEqualTo(emptyList<Media>())
    }
}