package com.baghdad.repository

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.util.executeAuthorizedSafely
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getRemotePagedSafely
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepositoryImpl @Inject constructor(
    val remoteGenreDataSource: RemoteGenreDataSource,
    val localSessionDataStore: LocalSessionDataStore,
    val tvShowRemoteDataSource: RemoteTvShowDataSource,
    val authenticationRepository: AuthenticationRepository
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

    override suspend fun getTvShowsByGenre(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<TvShow> {
        return getRemotePagedSafely(
            page = page, pageSize = pageSize,
            getRemoteData = { page, _ ->
                tvShowRemoteDataSource.getTvShowsByGenre(
                    genreId = genreId,
                    page = page,
                )
            },
        ) {
            it.toEntity()
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
        return executeSafely {
            tvShowRemoteDataSource.getTopRatedTvShows(page).toPagedResult {
                it.toEntity()
            }
        }
    }

    override suspend fun getPopularTvShows(): List<TvShow> {
        return executeSafely {
            tvShowRemoteDataSource.getPopularTvShows().map(TvShowDto::toEntity)
        }
    }

    override suspend fun getTrendingTvShows(page: Int): PagedResult<TvShow> {
        return executeSafely {
            tvShowRemoteDataSource.getTrendingTvShows(page).toPagedResult {
                it.toEntity()
            }
        }
    }

    override suspend fun addTvShowRate(
        tvShowId: Long,
        rating: Int
    ) {
        executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                tvShowRemoteDataSource.addTvShowRate(
                    tvShowId = tvShowId,
                    rating = rating,
                    sessionId = it
                )
            }
        )
    }

    override suspend fun deleteTvShowRate(tvShowId: Long) {
        executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                tvShowRemoteDataSource.deleteTvShowRate(
                    tvShowId = tvShowId,
                    sessionId = it
                )
            }
        )
    }

    override suspend fun getTvShowAccountStates(tvShowId: Long): MediaAccountStates {
        return executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                tvShowRemoteDataSource.getTvShowAccountStates(
                    tvShowId = tvShowId,
                    sessionId = it
                ).toEntity()
            }
        )
    }

    override suspend fun getUserRatedTvShows(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        val sessionId = localSessionDataStore.getSessionId()
        return getRemotePagedSafely(
            page = page, pageSize = pageSize,
            mapToEntity = { it.toMedia() },
            getRemoteData = { page, _ ->
                authenticationRepository.getLoggedInUser()?.let {
                    tvShowRemoteDataSource.getUserRatedTvShows(
                        it.id,
                        sessionId.toString(), page
                    )
                } ?: PagedResultDto(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = null
                )
            }
        )
    }

    companion object {
        private const val MAX_IMAGE_COUNT = 10
    }

}