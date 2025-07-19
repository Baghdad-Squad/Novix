package com.baghdad.repository

import android.util.Log
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import java.util.Locale

class MovieRepositoryImpl(
    val remoteGenreDataSource: RemoteGenreDataSource,
    val remoteMovieDataSource: RemoteMovieDataSource,
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
            Log.d("MovieRepositoryImpl", "getMovieDetails: $movieTrailer")
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
        Log.d("MovieRepositoryImpl", "getMovieReviews: $movieId")
        val result =  executeSafely {
            remoteMovieDataSource.getMovieReviews(movieId).map {
                it.toEntity()
            }
        }
        Log.d("MovieRepositoryImpl", "getMovieReviews: $result")
        return result
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        return executeSafely {
            remoteMovieDataSource.getMovieImages(movieId)
        }
    }
}