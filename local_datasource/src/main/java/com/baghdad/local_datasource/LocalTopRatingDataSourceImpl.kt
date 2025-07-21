package com.baghdad.local_datasource

import android.util.Log
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalTopRatingDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

class LocalTopRatingDataSourceImpl(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao
): LocalTopRatingDataSource {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return genreDao.getAllGenres().filter { it.type == GenreDto.GenreType.MOVIE.name }
            .map { it.toDto() }
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        pageSize: Int
    ): List<MovieDto> {
        val offset = calculatePageOffset(page, pageSize)

        return movieDao.getMovies(pageSize,offset).map {
            val genresDto = it.genres.map { genreId ->
                genreDao.getGenreById(genreId).toDto()
            }
            it.toDto(genresDto)
        }
    }

    override suspend fun saveTopRatedMovies(movieData: List<MovieDto>) {
        movieDao.upsertMovies(movieData.map { it.toLocalDto() })
    }

    override suspend fun deleteInvalidCachedMovies(timestamp: Long) {
        Log.e("LocalTopRatingDataSourceImpl", "deleteInvalidCachedMovies: $timestamp")
    }
}