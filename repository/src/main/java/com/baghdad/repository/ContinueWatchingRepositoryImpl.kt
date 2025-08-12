package com.baghdad.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ContinueWatchingDto
import com.baghdad.repository.model.GenreDto
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
class ContinueWatchingRepositoryImpl @Inject constructor(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    private val authenticationRepository: AuthenticationRepository,
    private val savableMovieDataSource: LocalSavableMovieDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource
) : ContinueWatchingRepository {

    override suspend fun getPagedMovies(page: Int, pageSize: Int): PagedResult<ContinueWatching> {
        return getPagedItems(
            page = page,
            pageSize = pageSize,
            fetchData = localContinueWatchingDataSource::getPagedContinueWatchingMovies
        )
    }

    override suspend fun getPagedTvShows(page: Int, pageSize: Int): PagedResult<ContinueWatching> {
        return getPagedItems(
            page = page,
            pageSize = pageSize,
            fetchData = localContinueWatchingDataSource::getPagedContinueWatchingTvShows
        )
    }

    override suspend fun observeContinueWatching(): Flow<List<ContinueWatching>> {
        val user = authenticationRepository.getLoggedInUser() ?: return flowOf(emptyList())
        val savedMovies = savableMovieDataSource.getSavedMovies()

        return localContinueWatchingDataSource
            .observeContinueWatching(user.id)
            .map { items -> items.map(mapDtoToEntityWithSavedMovies(savedMovies)) }
    }

    override suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType,
    ) = executeSafely {
        val userId = authenticationRepository.getLoggedInUser()?.id ?: return@executeSafely
        val item = ContinueWatching(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            userId = userId,
            isSaved = false,
            listId = null,
        )
        localContinueWatchingDataSource.addContinueWatching(item.toDto())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUsedMovieGenres(): Flow<List<Genre>> {
        return getUsedGenres(
            localDataCall = localContinueWatchingDataSource::getAllContinueWatchingMovies,
            remoteGenreCall = { remoteGenreDataSource.getMovieGenre(Locale.getDefault().language) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUsedTvShowGenres(): Flow<List<Genre>> {
        return getUsedGenres(
            localDataCall = localContinueWatchingDataSource::getAllContinueWatchingTvShows,
            remoteGenreCall = { remoteGenreDataSource.getTvShowGenre(Locale.getDefault().language) }
        )
    }


    private suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        fetchData: suspend (userId: Long, pageSize: Int, page: Int) -> List<ContinueWatchingDto>
    ): PagedResult<ContinueWatching> {
        val user =
            authenticationRepository.getLoggedInUser() ?: return PagedResult(emptyList(), 0, 0)
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
        localDataCall: suspend (userId: Long) -> Flow<List<ContinueWatchingDto>>,
        remoteGenreCall: suspend () -> List<GenreDto>
    ): Flow<List<Genre>> {
        val user = authenticationRepository.getLoggedInUser() ?: return flowOf(emptyList())
        val savedMovies = savableMovieDataSource.getSavedMovies()
        val remoteGenres = remoteGenreCall()

        return localDataCall(user.id)
            .mapLatest { items ->
                val entities = items.map(mapDtoToEntityWithSavedMovies(savedMovies))
                filterUsedGenres(entities, remoteGenres).map { it.toEntity() }
            }
    }

    private fun filterUsedGenres(
        items: List<ContinueWatching>,
        genres: List<GenreDto>
    ): List<GenreDto> {
        val usedGenreIds = items.flatMap { it.genreIds }.toSet()
        return genres.filter { it.id in usedGenreIds }
    }

    private fun mapDtoToEntityWithSavedMovies(
        savedMovies: Map<Long, Long>
    ): (ContinueWatchingDto) -> ContinueWatching = { dto ->
        dto.toEntity(
            isSaved = savedMovies.containsKey(dto.contentId),
            listId = savedMovies[dto.contentId],
        )
    }
}
