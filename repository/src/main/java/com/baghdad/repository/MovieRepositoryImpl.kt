package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalTopRatingDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteTopRatingDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getPagedSafely
import java.util.Locale

class MovieRepositoryImpl(
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localGenreDataSource: LocalGenreDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val remoteTopRatingDataSource: RemoteTopRatingDataSource,
    private val localTopRatingDataSource: LocalTopRatingDataSource,
) : MovieRepository {
    override suspend fun getGenres(): List<Genre> {
        return executeSafely {
            remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return executeSafely {
            remoteMovieDataSource.getSimilarMovies(movieId).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Long): Movie {
        return executeSafely {
            val movieTrailer = remoteMovieDataSource.getMovieTrailer(movieId)
            remoteMovieDataSource.getMovieDetails(movieId).toEntity()
                .copy(trailerURL = movieTrailer)
        }
    }

    override suspend fun getMovieCastMembers(movieId: Long): List<CastMember> {
        return executeSafely {
            remoteMovieDataSource.getMovieCastMembers(movieId).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getMoviesByGenre(
        genreId: Long,
        page: Int
    ): List<Movie> {
        return executeSafely {
            remoteMovieDataSource.getMoviesByGenre(genreId, page).map {
                it.toEntity()
            }
        }
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> {
        val result =  executeSafely {
            remoteMovieDataSource.getMovieReviews(movieId).map {
                it.toEntity()
            }
        }
        return result
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        return executeSafely {
            remoteMovieDataSource.getMovieImages(movieId)
        }
    }

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
                remoteTopRatingDataSource.getTopRatedMovies(page)

            },

            cacheData = { data ->
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
