package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.TopRatedDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDtos
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalTopRatingDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

class LocalTopRatingDataSourceImpl(
    private val topRatedDao: TopRatedDao,
    private val genreDao: GenreDao,
    private val logger: Logger
) : LocalTopRatingDataSource {

    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getAllGenres().filter { it.type == GenreDto.GenreType.MOVIE.name }
                .map { it.toDto() }
        }
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        pageSize: Int,
    ): List<MovieDto> {
        val offset = calculatePageOffset(page, pageSize)
        return executeWithErrorHandling(logger = logger) {
            topRatedDao.getTopRatedMoves(pageSize, offset).map {
                val genresDto = it.genres.map { genreId ->
                    genreDao.getGenreById(genreId).toDto()
                }
                it.toDto(genresDto)
            }
        }
    }

    override suspend fun saveTopRatedMovies(movieData: List<MovieDto>) {
        executeWithErrorHandling(logger = logger) {
            topRatedDao.upsertMovies(movieData.map { it.toLocalDtos() })
        }
    }
}