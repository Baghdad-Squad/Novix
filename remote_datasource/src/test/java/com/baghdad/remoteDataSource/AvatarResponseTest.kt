package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.response.user.AvatarResponse
import com.baghdad.remoteDataSource.response.user.GravatarResponse
import com.baghdad.remoteDataSource.response.user.TmdbResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AvatarResponseTest {

    @Test
    fun shouldReturnCorrectGravatarHashWhenProvided() {
        // Given
        val gravatar = GravatarResponse(hash = "abc123")
        val response = AvatarResponse(gravatar = gravatar)

        // When
        val result = response.gravatar?.hash

        // Then
        assertThat(result).isEqualTo("abc123")
    }

    @Test
    fun shouldReturnNullGravatarWhenNotProvided() {
        // Given
        val response = AvatarResponse(gravatar = null)

        // When
        val result = response.gravatar

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return correct tmdb avatar path when provided`() {
        // Given
        val tmdb = TmdbResponse(avatarPath = "/path/to/avatar.png")
        val response = AvatarResponse(tmdb = tmdb)

        // When
        val result = response.tmdb?.avatarPath

        // Then
        assertThat(result).isEqualTo("/path/to/avatar.png")
    }

    @Test
    fun `should return null tmdb when not provided`() {
        // Given
        val response = AvatarResponse(tmdb = null)

        // When
        val result = response.tmdb

        // Then
        assertThat(result).isNull()
    }
}