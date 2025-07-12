package com.baghdad.repository

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeSafely
import java.util.Locale

class TvShowRepositoryImpl(
    val remoteGenreDataSource: RemoteGenreDataSource
) : TvShowRepository {
    override suspend fun getGenres(): List<Genre> {
        return executeSafely {
            remoteGenreDataSource.getTvShowGenre(language = Locale.getDefault().language)
        }.map {
            it.toEntity()
        }
    }
}