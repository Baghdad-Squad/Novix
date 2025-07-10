package com.baghdad.repository

import com.baghdad.entity.media.Media
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class RecentlyViewedRepositoryImplTest {
    val recentlyViewedRepository = RecentlyViewedRepositoryImpl()

    @Test
    fun getAllRecentlyViewed() = runTest {
        assertThat(recentlyViewedRepository.getAllRecentlyViewed())
            .isEqualTo(emptyList<Media>())

    }

    @Test
    fun deleteAllRecentlyViewed() = runTest {
        assertDoesNotThrow {
            recentlyViewedRepository.deleteAllRecentlyViewed()

        }
    }

    @Test
    fun addMediaToRecentlyViewed() = runTest {
        assertDoesNotThrow {
            recentlyViewedRepository.addMediaToRecentlyViewed(1, "movie")
        }
    }

}