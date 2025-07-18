package com.baghdad.repository

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import kotlinx.datetime.LocalDate
import java.util.Locale

class TvShowRepositoryImpl(
    val remoteGenreDataSource: RemoteGenreDataSource
) : TvShowRepository {
    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return TvShow(
            id = 1,
            title = "title",
            genres = emptyList(),
            averageRating = 0.0,
            userRating = 0.0,
            releaseDate = LocalDate(2023, 1, 1),
            numberOfSeasons = 0,
            overview = "",
            posterImageURL = "",
            headerPictures = emptyList()
        )
//        TODO("Not yet implemented")
    }

    override suspend fun getTvShowCastMembers(tvShowId: Long): List<CastMember> {
        return emptyList()

//        TODO("Not yet implemented")
    }

    override suspend fun getTvShowSeasonEpisodes(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return emptyList()
//        TODO("Not yet implemented")
    }

    override suspend fun getGenres(): List<Genre> {
        return executeSafely {
            remoteGenreDataSource.getTvShowGenre(language = Locale.getDefault().language)
        }.map {
            it.toEntity()
        }
    }

    override suspend fun getTvShowsByGenres(genreId: Long): List<TvShow> {
        return emptyList()
//        TODO("Not yet implemented")
    }
}