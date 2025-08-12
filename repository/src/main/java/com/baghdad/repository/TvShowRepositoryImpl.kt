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
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getRemotePagedSafely
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepositoryImpl @Inject constructor(
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val tvShowRemoteDataSource: RemoteTvShowDataSource,
    private val authenticationRepository: AuthenticationRepository
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
            val tvShowImages = tvShowRemoteDataSource.getTvShowImages(tvShowId = tvId)
            val tvShowTrailer = tvShowRemoteDataSource.getTvShowTrailer(tvShowId = tvId)

            tvShowRemoteDataSource.getTvShowDetails(tvShowId = tvId)
                .toEntity()
                .copy(headerImagesURLs = tvShowImages, trailerURL = tvShowTrailer)
        }
    }

    override suspend fun getTvShowCastMembers(tvId: Long): List<CastMember> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowCastMembers(tvShowId = tvId).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTvShowImages(tvId: Long): List<String> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowImages(tvShowId = tvId)
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
            tvShowRemoteDataSource.getTvShowEpisodes(tvShowId = tvId, seasonNumber).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getTvShowReviews(tvId: Long): List<Review> {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowReviews(tvShowId = tvId).map {
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
            tvShowRemoteDataSource.getPopularTvShows().map(transform = TvShowDto::toEntity)
        }
    }

    override suspend fun getTrendingTvShows(page: Int): PagedResult<TvShow> {
        return executeSafely {
            tvShowRemoteDataSource.getTrendingTvShows(page).toPagedResult {
                it.toEntity()
            }
        }
    }

    override suspend fun addTvShowRate(tvShowId: Long, rating: Int) {
        executeSafely {
            tvShowRemoteDataSource.addTvShowRate(
                tvShowId = tvShowId,
                rating = rating,
            )
        }
    }

    override suspend fun deleteTvShowRate(tvShowId: Long) {
        executeSafely {
            tvShowRemoteDataSource.deleteTvShowRate(tvShowId = tvShowId)
        }
    }

    override suspend fun getTvShowAccountStates(tvShowId: Long): MediaAccountStates {
        return executeSafely {
            tvShowRemoteDataSource.getTvShowAccountStates(tvShowId = tvShowId).toEntity()
        }
    }

    override suspend fun getUserRatedTvShows(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        return executeSafely {
            getRemotePagedSafely(
                page = page, pageSize = pageSize,
                getRemoteData = { page, _ ->
                    authenticationRepository.getLoggedInUser()?.let {
                        tvShowRemoteDataSource.getUserRatedTvShows(accountId = it.id, page)
                    } ?: PagedResultDto(
                        data = emptyList(),
                        nextKey = null,
                        prevKey = null
                    )
                },
                mapToEntity = { it.toMedia() }
            )
        }
    }
}