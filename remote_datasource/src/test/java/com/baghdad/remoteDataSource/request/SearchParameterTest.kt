package com.baghdad.remoteDataSource.request

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchParameterTest {

    @Test
    fun `toParams should return correct map when all values are provided`() {
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
    fun `toParams should default page to 1 when pageNumber is null`() {
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
    fun `toParams should keep query empty when query is empty`() {
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
    fun `toParams should handle blank query as empty string`() {
        val param = SearchParameter(
            query = "   ",
            pageNumber = 2,
            includeAdult = false
        )

        val result = param.toParams()

        assertThat(result).isEqualTo(
            mapOf(
                "query" to "   ",
                "page" to "2",
                "include_adult" to "false"
            )
        )
    }

    @Test
    fun `toParams should handle negative and zero page numbers`() {
        val negativePage = SearchParameter("Test", -1, false).toParams()
        assertThat(negativePage["page"]).isEqualTo("-1")

        val zeroPage = SearchParameter("Test", 0, true).toParams()
        assertThat(zeroPage["page"]).isEqualTo("0")
        assertThat(zeroPage["include_adult"]).isEqualTo("true")
    }

    @Test
    fun `toParams should always return exactly 3 keys`() {
        val param = SearchParameter("Test", null, false)
        val result = param.toParams()

        assertThat(result.keys).containsExactly("query", "page", "include_adult")
        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `toParams should keep blank query`() {
        val param = SearchParameter("   ", 2, true)
        val result = param.toParams()

        assertThat(result["query"]).isEqualTo("   ")
        assertThat(result["page"]).isEqualTo("2")
        assertThat(result["include_adult"]).isEqualTo("true")
    }

    @Test
    fun `toParams should handle zero and negative page numbers`() {
        val zeroPage = SearchParameter("ZeroTest", 0, false).toParams()
        assertThat(zeroPage["page"]).isEqualTo("0")

        val negativePage = SearchParameter("NegativeTest", -5, true).toParams()
        assertThat(negativePage["page"]).isEqualTo("-5")
    }

}
