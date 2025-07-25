package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import java.util.Locale

class TvShowRepositoryImpl(
    val remoteGenreDataSource: RemoteGenreDataSource,
    val tvShowRemoteDataSource: RemoteTvShowDataSource
) : TvShowRepository {
    override suspend fun getGenres(): List<Genre> {
        return executeSafely {
            remoteGenreDataSource.getTvShowGenre(language = Locale.getDefault().language)
        }.map {
            it.toEntity()
        }
    }

    override suspend fun getTvShowDetails(tvId: Long): TvShow {
        return executeSafely {
            val tvShowImages = tvShowRemoteDataSource.getTvShowImages(tvId).take(MAX_IMAGE_COUNT)
            val tvShowTrailer = tvShowRemoteDataSource.getTvShowTrailer(tvId)
            tvShowRemoteDataSource.getTvShowDetails(tvId).toEntity()
                .copy(headerImagesURLs = tvShowImages, trailerURL = tvShowTrailer)
        }
    }

    override suspend fun getTvShowCastMembers(tvId: Long): List<CastMember> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowCastMembers(tvId).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTvShowImages(tvId: Long): List<String> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowImages(tvId)
        }
    }

    override suspend fun getTvShowsByGenre(genreId: Long, page: Int): List<TvShow> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowsByGenre(genreId, page).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTvShowSeasonEpisodes(tvId: Long, seasonNumber: Int): List<Episode> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTvShowReviews(tvId: Long): List<Review> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowReviews(tvId).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTopRatedTvShows(page: Int): PagedResult<TvShow> {
        val response = executeSafely {
            tvShowRemoteDataSource.getTopRatedTvShows(page)
        }
        return PagedResult(
            data = response.data.map { it.toEntity() },
            nextKey = response.nextKey,
            prevKey = response.prevKey,
        )
    }

    companion object {
        private const val MAX_IMAGE_COUNT = 10
    }

}