package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetGenresUseCaseTest {
    private val getGenresUseCase = GetGenresUseCase()

    @Test
    fun dummyTest() = runTest {
        Truth.assertThat(getGenresUseCase()).isEqualTo(emptyList<Genre>())
    }
}