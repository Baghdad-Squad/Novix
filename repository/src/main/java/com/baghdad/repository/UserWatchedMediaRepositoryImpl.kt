package com.baghdad.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.UserWatchedMediaDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.UserWatchedMediaDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getLocalPagedSafely
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserWatchedMediaRepositoryImpl @Inject constructor(
    private val userWatchedMediaDataSource: UserWatchedMediaDataSource,
    private val authenticationRepository: AuthenticationRepository,
    private val savableMovieDataSource: SavableMovieDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource
) : UserWatchedMediaRepository {

    override suspend fun getPagedMovies(page: Int, pageSize: Int): PagedResult<UserWatchedMedia> {
        return getPagedItems(
            page = page,
            pageSize = pageSize,
            fetchData = userWatchedMediaDataSource::getPagedUserWatchedMediaMovies
        )
    }

    override suspend fun getPagedTvShows(page: Int, pageSize: Int): PagedResult<UserWatchedMedia> {
        return getPagedItems(
            page = page,
            pageSize = pageSize,
            fetchData = userWatchedMediaDataSource::getPagedUserWatchedMediaTvShows
        )
    }

    override suspend fun observeUserWatchedMedia(): Flow<List<UserWatchedMedia>> {
        val user = authenticationRepository.getUserInfo() ?: return flowOf(emptyList())
        val savedMovies = savableMovieDataSource.getSavedMovies()

        return userWatchedMediaDataSource
            .observeUserWatchedMedia(user.id)
            .map { items -> items.map(mapDtoToEntityWithSavedMovies(savedMovies)) }
    }

    override suspend fun addUserWatchedMedia(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType,
    ) = executeSafely {
        val userId = authenticationRepository.getUserInfo()?.id ?: return@executeSafely
        val item = UserWatchedMedia(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            userId = userId,
            isSaved = false,
            listId = null,
        )
        userWatchedMediaDataSource.addUserWatchedMedia(item.toDto())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUsedMovieGenres(): Flow<List<Genre>> {
        return getUsedGenres(
            localDataCall = userWatchedMediaDataSource::getUserWatchedMediaMovies,
            remoteGenreCall = { remoteGenreDataSource.getMovieGenre(Locale.getDefault().language) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUsedTvShowGenres(): Flow<List<Genre>> {
        return getUsedGenres(
            localDataCall = userWatchedMediaDataSource::getUserWatchedMediaTvShows,
            remoteGenreCall = { remoteGenreDataSource.getTvShowGenre(Locale.getDefault().language) }
        )
    }


    private suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        fetchData: suspend (userId: Long, pageSize: Int, page: Int) -> List<UserWatchedMediaDto>
    ): PagedResult<UserWatchedMedia> {
        val user =
            authenticationRepository.getUserInfo() ?: return PagedResult(emptyList(), 0, 0)
        val savedMovies = savableMovieDataSource.getSavedMovies()

        return getLocalPagedSafely(
            page = page,
            pageSize = pageSize,
            getCachedPage = { page, _ -> fetchData(user.id, pageSize, page) },
            mapToEntity = mapDtoToEntityWithSavedMovies(savedMovies)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getUsedGenres(
        localDataCall: suspend (userId: Long) -> Flow<List<UserWatchedMediaDto>>,
        remoteGenreCall: suspend () -> List<GenreDto>
    ): Flow<List<Genre>> {
        val user = authenticationRepository.getUserInfo() ?: return flowOf(emptyList())
        val savedMovies = savableMovieDataSource.getSavedMovies()
        val remoteGenres = remoteGenreCall()

        return localDataCall(user.id)
            .mapLatest { items ->
                val entities = items.map(mapDtoToEntityWithSavedMovies(savedMovies))
                filterUsedGenres(entities, remoteGenres).map { it.toEntity() }
            }
    }

    private fun filterUsedGenres(
        items: List<UserWatchedMedia>,
        genres: List<GenreDto>
    ): List<GenreDto> {
        val usedGenreIds = items.flatMap { it.genreIds }.toSet()
        return genres.filter { it.id in usedGenreIds }
    }

    private fun mapDtoToEntityWithSavedMovies(
        savedMovies: Map<Long, Long>
    ): (UserWatchedMediaDto) -> UserWatchedMedia = { dto ->
        dto.toEntity(
            isSaved = savedMovies.containsKey(dto.contentId),
            listId = savedMovies[dto.contentId],
        )
    }
}
