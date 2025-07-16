package com.baghdad.local_datasource

import android.util.Log
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.util.executeSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieDataSourceImpl(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
) : LocalMovieDataSource {
    override suspend fun addMovie(movie: MovieDto) =
        executeWithErrorHandling {
            val movieEntity = movie.toLocalDto()
            movieDao.upsertMovie(movieEntity)
        }

    override suspend fun addMovies(movies: List<MovieDto>) {
        executeWithErrorHandling {
            Log.d("PagedResultPagingSource", "Adding ${movies.size} movies to the database")
            movieDao.upsertMovies(movies.map(MovieDto::toLocalDto))
        }
    }

    override suspend fun getMovieById(id: Long): MovieDto =
        executeWithErrorHandling {
            val movie = movieDao.getMovieById(id)
            val genresDto: List<GenreDto> = movie.genres.map {
                genreDao.getGenreById(it).toDto()
            }
            movie.toDto(genresDto)

        }

    override suspend fun getMoviesByIds(ids: List<Long>): List<MovieDto> {
        return executeWithErrorHandling {
            val movies = movieDao.getMoviesByIds(ids)
            val genresDto =
                genreDao.getAllGenres().filter { it.type == GenreDto.GenreType.MOVIE.name }
                    .map(Genre::toDto)
            movies.map { movie -> movie.toDto(genres = genresDto.filter { genre -> genre.id in movie.genres }) }
        }
    }

    override suspend fun getAllMovies(): Flow<List<MovieDto>> =
        executeWithErrorHandling {
            movieDao.getAllMovies().map {
                it.map {
                    val genresDto = it.genres.map {
                        genreDao.getGenreById(it).toDto()
                    }
                    it.toDto(genresDto)
                }
            }
        }

    override suspend fun deleteMovieById(id: Long) =
        executeWithErrorHandling {
            movieDao.deleteMovieById(id)
        }

    override suspend fun deleteAllMovies() =
        executeWithErrorHandling {
            movieDao.deleteAll()
        }

    override suspend fun updateMovie(newMovie: MovieDto) =
        executeWithErrorHandling {
            val movieEntity = newMovie.toLocalDto()
            movieDao.upsertMovie(movieEntity)
        }

    override suspend fun searchMoviesByTitle(title: String, page: Int?, pageSize: Int) =
        executeWithErrorHandling {
            val currentPage = page ?: 1
            val offset = (currentPage - 1) * pageSize
            Log.d(
                "LocalMovieDataSourceImpl",
                "Searching for movies with title: $title, page: $currentPage, offset: $offset"
            )
            movieDao.getMoviesFromSearchQuery(title, pageSize, offset).map {
                val genresDto = it.genres.map {
                    genreDao.getGenreById(it).toDto()
                }
                it.toDto(genresDto)
            }
        }

    override suspend fun getMovieCountByTitle(title: String): Int {
        return executeSafely {
            movieDao.getMovieCountBySearchQuery(title)
        }
    }
}
