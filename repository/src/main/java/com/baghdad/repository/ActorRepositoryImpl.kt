package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.mapper.toSavableMovie
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorRepositoryImpl @Inject constructor(
    private val remoteActorDataSource: RemoteActorDataSource,
    private val savableMovieDataSource: LocalSavableMovieDataSource,
) : ActorRepository {
    override suspend fun getActorInfo(actorId: Long): Actor {
        return executeSafely {
            remoteActorDataSource.getActorDetails(actorId).toEntity().copy(
                headerPictures = remoteActorDataSource.getActorImages(actorId).take(3)
            )
        }

    }

    override suspend fun getActorMovies(actorId: Long): List<SavableMovie> =
        executeSafely {
            val savedMovies = savableMovieDataSource.getSavedMovies()
            remoteActorDataSource.getActorMovies(actorId).map {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            }
        }

    override suspend fun getActorTvShows(actorId: Long): List<TvShow> {
        return executeSafely {
            remoteActorDataSource.getActorTvShows(actorId).map { it.toEntity() }
        }
    }

    override suspend fun getActorGallery(actorId: Long): List<String> {
        return executeSafely {
            remoteActorDataSource.getActorImages(actorId)
        }
    }

    override suspend fun getTrendingActors(page: Int): PagedResult<Actor> {
        return executeSafely {
            remoteActorDataSource.getTrendingActors(page).toPagedResult {
                it.toEntity()
            }
        }
    }
}
