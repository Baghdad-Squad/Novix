package com.baghdad.repository

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeSafely

class MovieRepositoryImpl(
    val remoteGenreDataSource: RemoteGenreDataSource
) : MovieRepository {
    override suspend fun getGenres(): List<Genre> {
        return executeSafely {
            remoteGenreDataSource.getMovieGenre(language = "en").map {
                it.toEntity()
            }
        }
    }
}