package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.repository.model.SearchQueryDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovie(movie: Movie)

    @Upsert
    suspend fun upsertMovies(movies: List<Movie>)

    @Query("DELETE FROM Movie WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

    @Query("DELETE FROM Movie")
    suspend fun deleteAll()

    @Query("SELECT * FROM Movie WHERE id =  :id")
    suspend fun getMovieById(id: Long): Movie

    @Query("SELECT * FROM Movie WHERE id IN (:ids)")
    suspend fun getMoviesByIds(ids: List<Long>): List<Movie>

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Query(
        """
    SELECT m.* FROM Movie m
    INNER JOIN search_query sq ON m.id = sq.mediaId
    WHERE sq.queryName = :queryName AND sq.mediaType = :mediaType
    LIMIT :pageSize OFFSET :offset
"""
    )
    suspend fun getMoviesFromSearchQuery(
        queryName: String,
        pageSize: Int,
        offset: Int,
        mediaType: String = SearchQueryDto.MediaType.MOVIE.name
    ): List<Movie>
}