package com.baghdad.repository

import android.util.Log
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TopRatingRepository
import com.baghdad.entity.media.Movie
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalTopRatingDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTopRatingDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.util.getPagedSafely
import java.util.Locale

class TopRatingRepositoryImpl(
    private val remoteTopRatingDataSource: RemoteTopRatingDataSource,
    private val localTopRatingDataSource: LocalTopRatingDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localGenreDataSource: LocalGenreDataSource,
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
                updateGenreCache()
                val genres = localGenreDataSource.getMovieGenre(Locale.getDefault().language)
                Log.d("TopRatingRepository", "🌐 Fetching remote movies for genres=$genres")
                remoteTopRatingDataSource.getTopRatedMovies(page)

            },

            cacheData = { data ->
                Log.d("TopRatingRepositoryImpl", "💾 Cached ${data} movies for page=$page")
                localTopRatingDataSource.saveTopRatedMovies(data)
            }

        )

    }

    private suspend fun updateGenreCache() {
        val lang = Locale.getDefault().language
        val movieGenres = remoteGenreDataSource.getMovieGenre(lang)
        movieGenres.forEach { localGenreDataSource.addGenre(it) }
    }
}
