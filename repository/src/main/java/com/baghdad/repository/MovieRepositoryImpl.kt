package com.baghdad.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toIsMediaRated
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.mapper.toSavableMovie
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getRemotePagedSafely
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val savableMovieDataSource: SavableMovieDataSource,
    private val authenticationRepository: AuthenticationRepository
) : MovieRepository {

    override suspend fun getGenres(): List<Genre> =
        executeSafely {
            remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language).map {
                it.toEntity()
            }
        }

    override suspend fun getMovieCastMembers(movieId: Long): List<CastMember> =
        executeSafely {
            remoteMovieDataSource.getMovieCastMembers(movieId = movieId).map {
                it.toEntity()
            }
        }

    override suspend fun getMovieImages(movieId: Long): List<String> =
        executeSafely {
            remoteMovieDataSource.getMovieImages(movieId = movieId)
        }

    override suspend fun getMovieStates(movieId: Long): Boolean {
        return executeSafely {
            remoteMovieDataSource.getMovieAccountStates(movieId = movieId).toIsMediaRated()
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): List<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getSimilarMovies(movieId = movieId).map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun deleteMovieRate(movieId: Long) {
        executeSafely {
            remoteMovieDataSource.deleteMovieRate(movieId = movieId)
        }
    }

    override suspend fun addMovieRate(movieId: Long, rating: Int) {
        executeSafely {
            remoteMovieDataSource.addMovieRate(movieId = movieId, rating = rating)
        }
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> =
        executeSafely {
            remoteMovieDataSource.getMovieReviews(movieId = movieId).map {
                it.toEntity()
            }
        }

    override suspend fun getTopRatedMovies(page: Int): PagedResult<SavedMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteMovieDataSource.getTopRatedMovies(page = page).toPagedResult {
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
            remoteMovieDataSource.getMovieDetails(movieId = movieId)
                .copy(trailerURL = movieTrailer)
                .toSavableMovie(
                    isSaved = savedMovies.containsKey(movieId),
                    listId = savedMovies[movieId],
                )
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
            remoteMovieDataSource.getUpcomingMovies(genreId = genreId).map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
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
                remoteMovieDataSource.getMoviesByGenre(genreId = genreId, page = page)
            },
        )
    }

    override suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia> =
        getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            getRemoteData = { page, _ ->
                authenticationRepository.getUserInfo()?.let {
                    remoteMovieDataSource.getUserRatedMovies(it.id, page = page)
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