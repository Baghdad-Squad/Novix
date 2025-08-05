//package com.baghdad.local_datasource.entity
//
//import com.baghdad.local_datasource.roomDB.entity.FavoriteGenre
//import com.baghdad.local_datasource.roomDB.entity.toDto
//import com.google.common.truth.Truth
//import org.junit.jupiter.api.Test
//
//class FavoriteGenreDtoTest {
//
//    @Test
//    fun `should return correct field values when LocalFavoriteGenre is created`() {
//        // Then
//        Truth.assertThat(LOCAL_FAVORITE_GENRE.genreId).isEqualTo(1L)
//        Truth.assertThat(LOCAL_FAVORITE_GENRE.name).isEqualTo("Action")
//        Truth.assertThat(LOCAL_FAVORITE_GENRE.count).isEqualTo(5)
//        Truth.assertThat(LOCAL_FAVORITE_GENRE.timeStamp).isEqualTo(123456789L)
//    }
//
//    @Test
//    fun `should apply default values correctly when count and timeStamp are not provided`() {
//        // Given
//        val localFavoriteGenre = FavoriteGenre(
//            genreId = 2L,
//            name = "Comedy"
//        )
//
//        // Then
//        Truth.assertThat(localFavoriteGenre.genreId).isEqualTo(2L)
//        Truth.assertThat(localFavoriteGenre.name).isEqualTo("Comedy")
//        Truth.assertThat(localFavoriteGenre.count).isEqualTo(0)
//        Truth.assertThat(localFavoriteGenre.timeStamp).isGreaterThan(0L)
//    }
//
//    @Test
//    fun `should convert LocalFavoriteGenre to DTO with all fields correctly`() {
//        // When
//        val result = LOCAL_FAVORITE_GENRE.toDto()
//
//        // Then
//        Truth.assertThat(result.genreId).isEqualTo(LOCAL_FAVORITE_GENRE.genreId)
//        Truth.assertThat(result.name).isEqualTo(LOCAL_FAVORITE_GENRE.name)
//        Truth.assertThat(result.count).isEqualTo(LOCAL_FAVORITE_GENRE.count)
//        Truth.assertThat(result.timeStamp).isEqualTo(LOCAL_FAVORITE_GENRE.timeStamp)
//    }
//
//    companion object {
//        val LOCAL_FAVORITE_GENRE = FavoriteGenre(
//            genreId = 1L,
//            name = "Action",
//            count = 5,
//            timeStamp = 123456789L
//        )
//    }
//}
