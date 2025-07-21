package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TopRatingRepository
import com.baghdad.entity.media.Movie
import com.baghdad.repository.datasource.local.LocalTopRatingDataSource
import com.baghdad.repository.datasource.remote.RemoteTopRatingDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.util.getPagedSafely
import java.util.Locale

class TopRatingRepositoryImpl(
    private val remoteTopRatingDataSource: RemoteTopRatingDataSource,
    private val localTopRatingDataSource: LocalTopRatingDataSource,
    ) : TopRatingRepository {

    override suspend fun getTopRatedMovies(page: Int): PagedResult<Movie> {

        return getPagedSafely(
            page = page,
            pageSize = 20,
            mapToEntity = MovieDto::toEntity,
            onStart = {  },
            getCachedPage = { page, pageSize ->
                localTopRatingDataSource.getTopRatedMovies(page, pageSize)
            },
            getRemoteData = { page, _ ->
                val genres = localTopRatingDataSource.getMovieGenre(Locale.getDefault().language)
                remoteTopRatingDataSource.getTopRatedMovies(page, genres)
            },
            cacheData = { data ->
                localTopRatingDataSource.saveTopRatedMovies(data)
            }
        )
    }

    private suspend fun deleteInvalidCacheOfMoreThanOneHour() {
        val oneHourBeforeNow = System.currentTimeMillis() - 3600000
        localTopRatingDataSource.deleteInvalidCachedMovies(oneHourBeforeNow)
    }
}
