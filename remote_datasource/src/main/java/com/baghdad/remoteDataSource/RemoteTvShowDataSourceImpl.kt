package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.episode.toDto
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.mapper.tvShow.mapToYoutubeURL
import com.baghdad.remoteDataSource.mapper.tvShow.toDto
import com.baghdad.remoteDataSource.mapper.tvShow.toPagedTvShowDtos
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto

class RemoteTvShowDataSourceImpl(
    private val tvShowApiService: TvShowApiService,
    private val logger: Logger,
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(tvId: Long): TvShowDto {
        val response = handleRequest<TVShowDetailsResponse>(
            apiCall = { tvShowApiService.getTvShowDetails(tvId) },
            logger = logger,
        )
        return response.toDto()
    }

    override suspend fun getTvShowCastMembers(tvId: Long): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = { tvShowApiService.getTvShowCastMembers(tvId) },
            logger = logger,
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getTvShowImages(tvId: Long): List<String> {
        return handleRequest<TVShowImagesResponse>(
            apiCall = { tvShowApiService.getTvShowImages(tvId) },
            logger = logger,
        ).backdrops.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getTvShowsByGenre(genreId: Long, page: Int): List<TvShowDto> {
        return handleRequest<TvShowResponse>(
            apiCall = { tvShowApiService.getTvShowsByGenre(genreId, page) },
            logger = logger,
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getTvShowEpisodes(tvId: Long, seasonNumber: Int): List<EpisodeDto> {
        return handleRequest<SeasonDetailResponse>(
            apiCall = { tvShowApiService.getTvShowEpisodes(tvId, seasonNumber) },
            logger = logger,
        ).episodes.orEmpty().map { it.toDto() }
    }

    override suspend fun getTvShowReviews(tvId: Long): List<ReviewDto> {
        return handleRequest<ReviewsResponse>(
            apiCall = { tvShowApiService.getTvShowReviews(tvId) },
            logger = logger,
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getTvShowTrailer(tvId: Long): String {
        return handleRequest<TVShowVideosResponse>(
            apiCall = { tvShowApiService.getTvShowTrailer(tvId) },
            logger = logger
        ).mapToYoutubeURL()
    }
    override suspend fun getTopRatedTvShows(page: Int): PagedResultDto<TvShowDto> {
        val response = handleRequest<TopRatedTvShowSearchResponse>(
            apiCall = { tvShowApiService.getTopRatedTvShows(page) },
            logger = logger,
        )
        return response.toPagedTvShowDtos()
    }


    override suspend fun getTrendingTvShows(page: Int): PagedResultDto<TvShowDto> {
        return handleRequest<TrendingTvShowsResponse>(
            apiCall = { tvShowApiService.getTrendingTvShows(page) },
            logger = logger,
        ).toPagedTvShowDtos()
    }
}
