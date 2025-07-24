package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.TrendingMovieDoa
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieDataSourceImpl(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val logger: Logger,
    private val trendingMovieDoa: TrendingMovieDoa
) : LocalMovieDataSource {
    override suspend fun addMovie(movie: MovieDto) =
        executeWithErrorHandling(logger = logger) {
            val movieEntity = movie.toLocalDto()
            movieDao.upsertMovie(movieEntity)
        }

    override suspend fun addMovies(movies: List<MovieDto>) {
        executeWithErrorHandling(logger = logger) {
            movieDao.upsertMovies(movies.map(MovieDto::toLocalDto))
        }
    }

    override suspend fun getMovieById(id: Long): MovieDto =
        executeWithErrorHandling(logger = logger) {
            val movie = movieDao.getMovieById(id)
            val genresDto: List<GenreDto> = movie.genres.map {
                genreDao.getGenreById(it).toDto()
            }
            movie.toDto(genresDto)

        }

    override suspend fun getMoviesByIds(ids: List<Long>): List<MovieDto> {
        return executeWithErrorHandling(logger = logger) {
            val movies = movieDao.getMoviesByIds(ids)
            val genresDto =
                genreDao.getAllGenres().filter { it.type == GenreDto.GenreType.MOVIE.name }
                    .map(Genre::toDto)
            movies.map { movie -> movie.toDto(genres = genresDto.filter { genre -> genre.id in movie.genres }) }
        }
    }

    override suspend fun getAllMovies(): Flow<List<MovieDto>> =
        executeWithErrorHandling(logger = logger) {
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
        executeWithErrorHandling(logger = logger) {
            movieDao.deleteMovieById(id)
        }

    override suspend fun deleteAllMovies() =
        executeWithErrorHandling(logger = logger) {
            movieDao.deleteAll()
        }

    override suspend fun updateMovie(newMovie: MovieDto) =
        executeWithErrorHandling(logger = logger) {
            val movieEntity = newMovie.toLocalDto()
            movieDao.upsertMovie(movieEntity)
        }

    override suspend fun searchMoviesByTitle(title: String, page: Int, pageSize: Int) =
        executeWithErrorHandling(logger = logger) {
            val pageOffset = calculatePageOffset(pageSize, page)
            val movies = movieDao.getMoviesFromSearchQuery(title, pageSize, pageOffset)
            val genresMap = getGenresMap(movies)
            movies.toDtos(genresMap)
        }

    private fun getGenresMap(
        movies: List<Movie>
    ): Map<Long, Genre> {
        val allGenreIds = movies.flatMap { it.genres }.distinct()
        return genreDao.getGenresByIds(allGenreIds).associateBy { it.id }
    }
}
