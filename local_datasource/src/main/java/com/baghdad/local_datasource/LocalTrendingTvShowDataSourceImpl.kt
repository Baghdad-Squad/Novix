package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.TrendingTvShowDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDtos
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalTrendingTvShowsDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto

class LocalTrendingTvShowDataSourceImpl(
    private val trendingTvShowDao: TrendingTvShowDao,
    private val genreDao: GenreDao,
    private val logger: Logger
) : LocalTrendingTvShowsDataSource {
    override suspend fun getTvShowsGenres(language: String): List<GenreDto> {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getAllGenres().filter { it.type == GenreDto.GenreType.TV_SHOW.name }
                .map { it.toDto() }
        }
    }

    override suspend fun getTrendingTvShows(
        page: Int,
        pageSize: Int
    ): List<TvShowDto> {
        val offset = calculatePageOffset(page = page, pageSize = pageSize)
        return executeWithErrorHandling(logger = logger) {
            trendingTvShowDao.getTrendingTvShows(pageSize, offset).map {
                val genresDto = it.genres.map { genreId ->
                    genreDao.getGenreById(genreId).toDto()
                }
                it.toDto(genresDto)
            }
        }
    }

    override suspend fun saveTrendingTvShows(tvShows: List<TvShowDto>) {
        executeWithErrorHandling(logger = logger) {
            trendingTvShowDao.upsertTvShow(tvShows.map { it.toLocalDtos() })
        }
    }
}