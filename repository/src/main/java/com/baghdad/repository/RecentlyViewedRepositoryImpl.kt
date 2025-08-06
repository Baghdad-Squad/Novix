package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentlyViewedRepositoryImpl @Inject constructor(
    val localRecentlyViewedDataSource: LocalRecentlyViewedDataSource,
    val localFavoriteGenreDataSource: LocalFavoriteGenreDataSource,
    val localMovieDataSource: LocalMovieDataSource,
    val localTvShowDataSource: LocalTvShowDataSource
) : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>> {
        return getFlowSafely {
            localRecentlyViewedDataSource.getAllRecentlyViewed()
                .map { it.map(RecentlyViewedDto::toEntity) }
        }
    }

    override suspend fun deleteAllRecentlyViewed() {
        executeSafely {
            localRecentlyViewedDataSource.deleteAllRecentlyViewed()
        }
    }

    override suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed) {
        executeSafely {
            updateFavoriteGenre(
                mediaId = recentlyViewed.contentId,
                mediaType = recentlyViewed.contentType
            )
            localRecentlyViewedDataSource.addMediaToRecentlyViewed(
                recentlyViewed.toDto()
            )
        }
    }

    private suspend fun updateFavoriteGenre(mediaId: Long, mediaType: RecentlyViewed.ContentType) {
        val mediaGenres: List<GenreDto> = when (mediaType) {
            RecentlyViewed.ContentType.MOVIE -> {
                localMovieDataSource.getMovieById(mediaId).genres
            }

            RecentlyViewed.ContentType.TV_SHOW -> {
                localTvShowDataSource.getTvShowById(mediaId).genres
            }
        }
        if (mediaGenres.isNotEmpty()) {
            mediaGenres.forEach {
                localFavoriteGenreDataSource.updateFavoriteGenreCount(it.id, it.name)
            }
        }

    }
}