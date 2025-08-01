package com.baghdad.viewmodel.mapper

import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieDetailsMapperTest {

    @Test
    fun `roundToFirstDecimal should round correctly when it called`() {
        assertThat(8.56.roundToFirstDecimal()).isEqualTo(8.6)
        assertThat(8.54.roundToFirstDecimal()).isEqualTo(8.5)
        assertThat(8.0.roundToFirstDecimal()).isEqualTo(8.0)
        assertThat(0.0.roundToFirstDecimal()).isEqualTo(0.0)
    }

}