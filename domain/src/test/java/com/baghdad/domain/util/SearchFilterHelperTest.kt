package com.baghdad.domain.util

import com.baghdad.entity.media.Genre
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SearchFilterHelperTest {

    private lateinit var helper: SearchFilterHelper

    @BeforeEach
    fun setUp() {
        helper = SearchFilterHelper()
    }

    @Test
    fun `matchesGenreFilter() should return true when selectedGenres is empty`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action")),
            selectedGenres = emptyList()
        )
        assertTrue(result)
    }

    @Test
    fun `matchesGenreFilter() should return true when item has matching genre`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action"), Genre(2, "Drama")),
            selectedGenres = listOf(Genre(2, "Drama"))
        )
        assertTrue(result)
    }

    @Test
    fun `matchesGenreFilter() should return false when no genres match`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action")),
            selectedGenres = listOf(Genre(2, "Drama"))
        )
        assertFalse(result)
    }

    @Test
    fun `matchesYearFilter() should return true when within min and max`() {
        val result = helper.matchesYearFilter(2020, 2010, 2022)
        assertTrue(result)
    }

    @Test
    fun `matchesYearFilter() should return false when below min`() {
        val result = helper.matchesYearFilter(2005, 2010, 2022)
        assertFalse(result)
    }

    @Test
    fun `matchesYearFilter() should return false when above max`() {
        val result = helper.matchesYearFilter(2025, 2010, 2022)
        assertFalse(result)
    }

    @Test
    fun `matchesYearFilter() should return true when no min and max`() {
        val result = helper.matchesYearFilter(2022, null, null)
        assertTrue(result)
    }

    @Test
    fun `matchesRatingFilter() should return true when rating is equal to minimum`() {
        val result = helper.matchesRatingFilter(7.0, 7)
        assertTrue(result)
    }

    @Test
    fun `matchesRatingFilter() should return true when rating is above minimum`() {
        val result = helper.matchesRatingFilter(8.5, 7)
        assertTrue(result)
    }

    @Test
    fun `matchesRatingFilter() should return false when rating is below minimum`() {
        val result = helper.matchesRatingFilter(6.0, 7)
        assertFalse(result)
    }
}
