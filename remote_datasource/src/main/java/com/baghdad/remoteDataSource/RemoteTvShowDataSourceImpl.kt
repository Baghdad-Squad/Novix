package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.episode.toDto
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto
import io.ktor.client.HttpClient

class RemoteTvShowDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(tvId: Long): TvShowDto {
        val endpoint = TV_SHOW_DETAILS_ENDPOINT.replace("{tv_id}", tvId.toString())
        val response = handleRequest<TVShowDetailsResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        )
        return response.toDto()
    }

    override suspend fun getTvShowCastMembers(tvId: Long): List<CastMemberDto> {
        val endpoint = TV_SHOW_CREDITS_ENDPOINT.replace("{tv_id}", tvId.toString())
        return handleRequest<CastMembersResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getTvShowImages(tvId: Long): List<String> {
        val endpoint = TV_SHOW_IMAGES_ENDPOINT.replace("{tv_id}", tvId.toString())
        return handleRequest<TVShowImagesResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).backdrops.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getTvShowsByGenre(genreId: Long, page: Int): List<TvShowDto> {
        val endpoint = TV_SHOW_WITH_GENRE_ENDPOINT
        return handleRequest<TvShowResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint?with_genres=$genreId&page=$page"
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getTvShowEpisodes(tvId: Long, seasonNumber: Int): List<EpisodeDto> {
        val endpoint = TV_SHOW_EPISODES_ENDPOINT
            .replace("{tv_id}", tvId.toString())
            .replace("{season_number}", seasonNumber.toString())
        return handleRequest<SeasonDetailResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).episodes.orEmpty().map { it.toDto() }
    }
    override suspend fun getTvShowReviews(tvId: Long): List<ReviewDto> {
        val endpoint = TV_SHOW_REVIEWS_ENDPOINT.replace("{tv_id}", tvId.toString())
        return handleRequest<ReviewsResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).results.orEmpty().map { it.toDto() }
    }


    companion object {
        private const val TV_SHOW_DETAILS_ENDPOINT = "/tv/{tv_id}"
        private const val TV_SHOW_CREDITS_ENDPOINT = "/tv/{tv_id}/credits"
        private const val TV_SHOW_IMAGES_ENDPOINT = "/tv/{tv_id}/images"
        private const val TV_SHOW_EPISODES_ENDPOINT = "/tv/{tv_id}/season/{season_number}"
        private const val TV_SHOW_WITH_GENRE_ENDPOINT = "/discover/tv"
        private const val TV_SHOW_REVIEWS_ENDPOINT = "/tv/{tv_id}/reviews"
}
}
