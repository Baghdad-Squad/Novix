package com.baghdad.repository

import android.util.Log
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.util.executeAuthorizedSafely
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getRemotePagedSafely
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localSessionDataStore: LocalSessionDataStore,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val authenticationRepository: AuthenticationRepository
) : MovieRepository {
    override suspend fun getGenres(): List<Genre> =
        executeSafely {
            remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language).map {
                it.toEntity()
            }
        }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> =
        executeSafely {
            remoteMovieDataSource.getSimilarMovies(movieId).map {
                it.toEntity()
            }
        }

    override suspend fun getMovieDetails(movieId: Long): Movie =
        executeSafely {
            val movieTrailer = remoteMovieDataSource.getMovieTrailer(movieId)
            remoteMovieDataSource
                .getMovieDetails(movieId)
                .toEntity()
                .copy(trailerURL = movieTrailer)
        }

    override suspend fun getMovieCastMembers(movieId: Long): List<CastMember> =
        executeSafely {
            remoteMovieDataSource.getMovieCastMembers(movieId).map {
                it.toEntity()
            }
        }

    override suspend fun getMoviesByGenre(
        genreId: Long,
        page: Int,
        pageSize: Int,
    ): PagedResult<Movie> =
        getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = MovieDto::toEntity,
            getRemoteData = { page, _ ->
                remoteMovieDataSource.getMoviesByGenre(genreId, page)
            },
        )

    override suspend fun getMovieReviews(movieId: Long): List<Review> {
        val result =
            executeSafely {
                remoteMovieDataSource.getMovieReviews(movieId).map {
                    it.toEntity()
                }
            }
        return result
    }

    override suspend fun getMovieImages(movieId: Long): List<String> =
        executeSafely {
            remoteMovieDataSource.getMovieImages(movieId).take(MAX_MOVIES_IMAGES)
        }

    override suspend fun getTopRatedMovies(page: Int): PagedResult<Movie> =
        executeSafely {
            remoteMovieDataSource.getTopRatedMovies(page).toPagedResult(MovieDto::toEntity)
        }

    override suspend fun getPopularMovies(): List<Movie> =
        executeSafely {
            remoteMovieDataSource.getPopularMovies().map(MovieDto::toEntity)
        }

    override suspend fun getTrendingMovies(page: Int): PagedResult<Movie> =
        executeSafely {
            remoteMovieDataSource.getTrendingMovies(page).toPagedResult {
                it.toEntity()
            }
        }

    override suspend fun getUpcomingMovies(genreId: Long?): List<Movie> =
        executeSafely {
            remoteMovieDataSource.getUpcomingMovies(genreId).map(MovieDto::toEntity)
        }

    override suspend fun addMovieRate(movieId: Long, rating: Int) {
        executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                remoteMovieDataSource.addMovieRate(
                    movieId = movieId,
                    rating = rating,
                    sessionId = it
                )
            }
        )
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                remoteMovieDataSource.deleteMovieRate(
                    movieId = movieId,
                    sessionId = it
                )
            }
        )
    }

    override suspend fun getMovieStates(movieId: Long): MediaAccountStates {
        return executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = {
                remoteMovieDataSource.getMovieAccountStates(
                    movieId = movieId,
                    sessionId = it
                ).toEntity()
            }
        )
    }

    override suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        return executeAuthorizedSafely(
            sessionId = localSessionDataStore.getSessionId(),
            block = { sessionId ->
                getRemotePagedSafely(
                    page = page, pageSize = pageSize,
                    getRemoteData = { page, _ ->
                        authenticationRepository.getLoggedInUser()?.let {
                            remoteMovieDataSource.getUserRatedMovies(it.id, sessionId ,page)
                        } ?: PagedResultDto(
                            data = emptyList(),
                            nextKey = null,
                            prevKey = null
                        )
                    },
                ) {
                    it.toMedia()
                }
            }
        )

    }

    companion object {
        private const val MAX_MOVIES_IMAGES = 10
    }
}
