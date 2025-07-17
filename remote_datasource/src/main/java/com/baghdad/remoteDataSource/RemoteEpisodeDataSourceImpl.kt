package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import io.ktor.client.HttpClient
import kotlin.collections.map
import kotlin.collections.orEmpty

class RemoteEpisodeDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
): RemoteEpisodeDataSource {

    override suspend fun getEpisodeDetails(tvId: Long, seasonNumber: Int, episodeNumber: Int): EpisodeDto {
        val endpoint = EPISODES_DETAILS_ENDPOINT
            .replace("{tv_id}", tvId.toString())
            .replace("{season_number}", seasonNumber.toString())
            .replace("{episode_number}", episodeNumber.toString())
        return handleRequest<EpisodeDto>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        )
    }

    override suspend fun getEpisodeCastMembers(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<CastMemberDto> {
        val endpoint = EPISODE_CREDITS_ENDPOINT
            .replace("{tv_id}", tvId.toString())
            .replace("{season_number}", seasonNumber.toString())
            .replace("{episode_number}", episodeNumber.toString())
        return handleRequest<CastMembersResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).cast?.map { it.toDto() } ?: emptyList()

    }

    override suspend fun getEpisodeImages(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<String> {
        val endpoint = EPISODE_IMAGES_ENDPOINT
            .replace("{tv_id}", tvId.toString())
            .replace("{season_number}", seasonNumber.toString())
            .replace("{episode_number}", episodeNumber.toString())
        return handleRequest<EpisodeImageResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).stills.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }
    companion object{
        private const val EPISODES_DETAILS_ENDPOINT = "/tv/{tv_id}/season/{season_number}/episode/{episode_number}"
        private const val EPISODE_CREDITS_ENDPOINT = "/tv/{tv_id}/season/{season_number}/episode/{episode_number}/credits"
        private const val EPISODE_IMAGES_ENDPOINT = "/tv/{tv_id}/season/{season_number}/episode/{episode_number}/images"

    }
}