package com.baghdad.remoteDataSource.request

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchParameterTest {

    @Test
    fun `toParams should return correct map when all values are provided`() {
        val param = SearchParameter(
            query = "Breaking Bad",
            pageNumber = 5,
            includeAdult = true
        )

        val result = param.toParams()

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
        val param = SearchParameter(
            query = "Game of Thrones",
            pageNumber = null,
            includeAdult = false
        )

        val result = param.toParams()

        assertThat(result).isEqualTo(
            mapOf(
                "query" to "Game of Thrones",
                "page" to "1",
                "include_adult" to "false"
            )
        )
    }
}
