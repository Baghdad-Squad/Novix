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
    fun `matchesGenreFilter returns true when selectedGenres is empty`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action")),
            selectedGenres = emptyList()
        )
        assertTrue(result)
    }

    @Test
    fun `matchesGenreFilter returns true when item has matching genre`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action"), Genre(2, "Drama")),
            selectedGenres = listOf(Genre(2, "Drama"))
        )
        assertTrue(result)
    }

    @Test
    fun `matchesGenreFilter returns false when no genres match`() {
        val result = helper.matchesGenreFilter(
            itemGenres = listOf(Genre(1, "Action")),
            selectedGenres = listOf(Genre(2, "Drama"))
        )
        assertFalse(result)
    }

    // --- Year Filter Tests ---

    @Test
    fun `matchesYearFilter returns true when within min and max`() {
        val result = helper.matchesYearFilter(2020, 2010, 2022)
        assertTrue(result)
    }

    @Test
    fun `matchesYearFilter returns false when below min`() {
        val result = helper.matchesYearFilter(2005, 2010, 2022)
        assertFalse(result)
    }

    @Test
    fun `matchesYearFilter returns false when above max`() {
        val result = helper.matchesYearFilter(2025, 2010, 2022)
        assertFalse(result)
    }

    @Test
    fun `matchesYearFilter returns true when no min and max`() {
        val result = helper.matchesYearFilter(2022, null, null)
        assertTrue(result)
    }

    // --- Rating Filter Tests ---

    @Test
    fun `matchesRatingFilter returns true when rating is equal to minimum`() {
        val result = helper.matchesRatingFilter(7.0, 7)
        assertTrue(result)
    }

    @Test
    fun `matchesRatingFilter returns true when rating is above minimum`() {
        val result = helper.matchesRatingFilter(8.5, 7)
        assertTrue(result)
    }

    @Test
    fun `matchesRatingFilter returns false when rating is below minimum`() {
        val result = helper.matchesRatingFilter(6.0, 7)
        assertFalse(result)
    }
}
