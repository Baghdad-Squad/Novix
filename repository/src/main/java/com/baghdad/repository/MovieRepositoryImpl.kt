package com.baghdad.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toIsMediaRated
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.mapper.toSavableMovie
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
    private val localSessionDataSource: LocalSessionDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val savableMovieDataSource: LocalSavableMovieDataSource,
    private val authenticationRepository: AuthenticationRepository
) : MovieRepository {
    override suspend fun getGenres(): List<Genre> =
        executeSafely {
            remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language).map {
                it.toEntity()
            }
        }

    override suspend fun getSimilarMovies(movieId: Long): List<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getSimilarMovies(movieId).map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun getMovieDetails(movieId: Long): SavedMovie =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            val movieTrailer = remoteMovieDataSource.getMovieTrailer(movieId)
            remoteMovieDataSource
                .getMovieDetails(movieId)
                .copy(trailerURL = movieTrailer)
                .toSavableMovie(
                    isSaved = savedMovies.containsKey(movieId),
                    listId = savedMovies[movieId],
                )
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
    ): PagedResult<SavedMovie> {
        val savedMovies = savableMovieDataSource.getSavedMovies()
        return getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            },
            getRemoteData = { page, _ ->
                remoteMovieDataSource.getMoviesByGenre(genreId, page)
            },
        )
    }

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

    override suspend fun getTopRatedMovies(page: Int): PagedResult<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getTopRatedMovies(page).toPagedResult {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun getPopularMovies(): List<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getPopularMovies().map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun getTrendingMovies(page: Int): PagedResult<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getTrendingMovies(page).toPagedResult {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun getUpcomingMovies(genreId: Long?): List<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getUpcomingMovies(genreId).map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun addMovieRate(movieId: Long, rating: Int) {
        executeAuthorizedSafely(
            sessionId = localSessionDataSource.getSessionId(),
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
            sessionId = localSessionDataSource.getSessionId(),
            block = {
                remoteMovieDataSource.deleteMovieRate(
                    movieId = movieId,
                    sessionId = it
                )
            }
        )
    }

    override suspend fun getMovieStates(movieId: Long): Boolean {
        return executeAuthorizedSafely(
            sessionId = localSessionDataSource.getSessionId(),
            block = {
                remoteMovieDataSource.getMovieAccountStates(
                    movieId = movieId,
                    sessionId = it
                ).toIsMediaRated()
            }
        )
    }

    override suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        return executeAuthorizedSafely(
            sessionId = localSessionDataSource.getSessionId(),
            block = { sessionId ->
                getRemotePagedSafely(
                    page = page, pageSize = pageSize,
                    getRemoteData = { page, _ ->
                        authenticationRepository.getUserInfo()?.let {
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
