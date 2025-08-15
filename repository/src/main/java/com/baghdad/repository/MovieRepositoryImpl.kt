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
import com.baghdad.repository.model.MovieDto
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
            remoteMovieDataSource.getMovieAccountStates(
                movieId = movieId
            ).toIsMediaRated()
        }
    }


    override suspend fun getSimilarMovies(movieId: Long): List<SavedMovie> =
        executeWithSavedMovies { savedMovies ->
            remoteMovieDataSource.getSimilarMovies(movieId = movieId).map {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
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
        executeWithSavedMovies { savedMovies ->
            remoteMovieDataSource.getTopRatedMovies(page = page).toPagedResult {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
            }
        }

    override suspend fun getMovieDetails(movieId: Long): SavedMovie =
        executeWithSavedMovies { savedMovies ->
            val movieTrailer = remoteMovieDataSource.getMovieTrailer(movieId)
            remoteMovieDataSource.getMovieDetails(movieId = movieId)
                .copy(trailerURL = movieTrailer)
                .let { mapToSavableMovie(savedMovies = savedMovies, movie = it) }
        }

    override suspend fun getPopularMovies(): List<SavedMovie> =
        executeWithSavedMovies { savedMovies ->
            remoteMovieDataSource.getPopularMovies().map {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
            }
        }

    override suspend fun getTrendingMovies(page: Int): PagedResult<SavedMovie> =
        executeWithSavedMovies { savedMovies ->
            remoteMovieDataSource.getTrendingMovies(page).toPagedResult {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
            }
        }

    override suspend fun getUpcomingMovies(genreId: Long?): List<SavedMovie> =
        executeWithSavedMovies { savedMovies ->
            remoteMovieDataSource.getUpcomingMovies(genreId = genreId).map {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
            }
        }

    override suspend fun getMoviesByGenre(
        genreId: Long,
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedMovie> {
        val savedMovies = getSavedMoviesMap()
        return getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = {
                mapToSavableMovie(savedMovies = savedMovies, movie = it)
            },
            getRemoteData = { page, _ ->
                remoteMovieDataSource.getMoviesByGenre(genreId = genreId, page = page)
            },
        )
    }

    override suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia> =
        executeSafely {
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

    private suspend fun getSavedMoviesMap() = savableMovieDataSource.getSavedMovies()

    private fun mapToSavableMovie(
        savedMovies: Map<Long, Long?>,
        movie: MovieDto
    ): SavedMovie {
        return movie.toSavableMovie(
            isSaved = savedMovies.containsKey(movie.id),
            listId = savedMovies[movie.id],
        )
    }

    private suspend fun <T> executeWithSavedMovies(
        block: suspend (Map<Long, Long?>) -> T
    ): T = executeSafely {
        val savedMovies = getSavedMoviesMap()
        block(savedMovies)
    }
}