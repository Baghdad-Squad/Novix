package com.baghdad.remoteDataSource.request

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchParameterTest {

    @Test
    fun `should return correct map when all values are provided`() {
        // Given
        val param = SearchParameter(
            query = "Breaking Bad",
            pageNumber = 5,
            includeAdult = true
        )

        // When
        val result = param.toParams()

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                "query" to "Breaking Bad",
                "page" to "5",
                "include_adult" to "true"
            )
        )
    }

    @Test
    fun `should default page to 1 when pageNumber is null`() {
        // Given
        val param = SearchParameter(
            query = "Game of Thrones",
            pageNumber = null,
            includeAdult = false
        )

        // When
        val result = param.toParams()

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                "query" to "Game of Thrones",
                "page" to "1",
                "include_adult" to "false"
            )
        )
    }

    @Test
    fun `should keep empty query when query is empty string`() {
        // Given
        val param = SearchParameter(
            query = "",
            pageNumber = 3,
            includeAdult = false
        )

        // When
        val result = param.toParams()

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                "query" to "",
                "page" to "3",
                "include_adult" to "false"
            )
        )
    }

    @Test
    fun `should keep blank query when query contains only spaces`() {
        // Given
        val param = SearchParameter(
            query = "   ",
            pageNumber = 2,
            includeAdult = false
        )

        // When
        val result = param.toParams()

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                "query" to "   ",
                "page" to "2",
                "include_adult" to "false"
            )
        )
    }

    @Test
    fun `should handle negative and zero page numbers correctly when they are provided`() {
        // Given
        val negativeParam = SearchParameter("Test", -1, false)
        val zeroParam = SearchParameter("Test", 0, true)

        // When
        val negativeResult = negativeParam.toParams()
        val zeroResult = zeroParam.toParams()

        // Then
        assertThat(negativeResult["page"]).isEqualTo("-1")
        assertThat(zeroResult["page"]).isEqualTo("0")
        assertThat(zeroResult["include_adult"]).isEqualTo("true")
    }

    @Test
    fun `should always return exactly 3 keys when toParams is called`() {
        // Given
        val param = SearchParameter("Test", null, false)

        // When
        val result = param.toParams()

        // Then
        assertThat(result.keys).containsExactly("query", "page", "include_adult")
        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `should keep blank query and include correct values when query is blank`() {
        // Given
        val param = SearchParameter("   ", 2, true)

        // When
        val result = param.toParams()

        // Then
        assertThat(result["query"]).isEqualTo("   ")
        assertThat(result["page"]).isEqualTo("2")
        assertThat(result["include_adult"]).isEqualTo("true")
    }

    @Test
    fun `should handle zero and negative page numbers independently when provided`() {
        // Given
        val zeroParam = SearchParameter("ZeroTest", 0, false)
        val negativeParam = SearchParameter("NegativeTest", -5, true)

        // When
        val zeroResult = zeroParam.toParams()
        val negativeResult = negativeParam.toParams()

        // Then
        assertThat(zeroResult["page"]).isEqualTo("0")
        assertThat(negativeResult["page"]).isEqualTo("-5")
    }
}
