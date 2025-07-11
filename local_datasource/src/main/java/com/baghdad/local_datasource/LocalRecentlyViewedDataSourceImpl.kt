package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentViewedDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.RecentViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toMediaDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.model.MediaDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import kotlinx.coroutines.flow.first

class LocalRecentlyViewedDataSourceImpl(
    private val recentViewedDao: RecentViewedDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao
) : LocalRecentlyViewedDataSource {
    override suspend fun getAllRecentlyViewed(): List<MediaDto> =
        executeWithErrorHandling {

            val recentItems = recentViewedDao.getAllRecentViewed().first()
            val movies = movieDao.getAllMovies().first().associateBy { it.id }
            val tvShows = tvShowDao.getAllTvShow().first().associateBy { it.id }

            recentItems.mapNotNull { recentItem ->
                when (recentItem.mediaType.lowercase()) {
                    "movie" -> movies[recentItem.mediaId]?.toDto()
                    "tvshow", "tv" -> tvShows[recentItem.mediaId]?.toMediaDto()
                    else -> null
                }
            }.sortedByDescending {

                recentItems.indexOfFirst { item ->
                    when (val dto = it) {
                        is MovieDto -> item.mediaId == dto.id && item.mediaType.equals(
                            "movie",
                            true
                        )

                        is TvShowDto -> item.mediaId == dto.id && item.mediaType.equals(
                            "tvshow",
                            true
                        )

                        else -> false
                    }
                }
            }
        }

    override suspend fun deleteAllRecentlyViewed() =
        executeWithErrorHandling {
            recentViewedDao.clearAllRecentViewed()
        }

    override suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: String) =
        executeWithErrorHandling {
            val media = RecentViewed(
                mediaId = mediaId,
                mediaType = mediaType
            )
            recentViewedDao.upsertRecentViewed(media)
        }

}