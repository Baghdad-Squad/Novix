package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.mapper.castMembers.toCastMembers
import com.baghdad.remoteDataSource.mapper.episode.toEpisodeDto
import com.baghdad.remoteDataSource.mapper.mediaAccountStates.toDto
import com.baghdad.remoteDataSource.mapper.review.toReviewDto
import com.baghdad.remoteDataSource.mapper.tvShow.mapToYoutubeURL
import com.baghdad.remoteDataSource.mapper.tvShow.toDto
import com.baghdad.remoteDataSource.mapper.tvShow.toImageUrls
import com.baghdad.remoteDataSource.mapper.tvShow.toPagedTvShowDtos
import com.baghdad.remoteDataSource.mapper.tvShow.toTvShowDtos
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
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
import com.baghdad.repository.model.MediaAccountStateDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTvShowDataSourceImpl @Inject constructor(
    private val tvShowApiService: TvShowApiService,
    private val logger: Logger,
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(tvShowId: Long): TvShowDto {
        return handleRequest<TVShowDetailsResponse>(
            apiCall = { tvShowApiService.getTvShowDetails(tvShowId = tvShowId) },
            logger = logger,
        ).toDto()
    }

    override suspend fun getTvShowCastMembers(tvShowId: Long): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = { tvShowApiService.getTvShowCastMembers(tvShowId = tvShowId) },
            logger = logger,
        ).toCastMembers()
    }

    override suspend fun getTvShowImages(tvShowId: Long): List<String> {
        return handleRequest<TVShowImagesResponse>(
            apiCall = { tvShowApiService.getTvShowImages(tvShowId = tvShowId) },
            logger = logger,
        ).toImageUrls()
    }

    override suspend fun getTvShowsByGenre(genreId: Long, page: Int): PagedResultDto<TvShowDto> {
        return handleRequest<TvShowResponse>(
            apiCall = { tvShowApiService.getTvShowsByGenre(genreId = genreId, page = page) },
            logger = logger,
        ).toPagedTvShowDtos()
    }

    override suspend fun getTvShowEpisodes(tvShowId: Long, seasonNumber: Int): List<EpisodeDto> {
        return handleRequest<SeasonDetailResponse>(
            apiCall = {
                tvShowApiService.getTvShowEpisodes(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber
                )
            },
            logger = logger,
        ).toEpisodeDto()
    }

    override suspend fun getTvShowReviews(tvShowId: Long): List<ReviewDto> {
        return handleRequest<ReviewsResponse>(
            apiCall = { tvShowApiService.getTvShowReviews(tvShowId = tvShowId) },
            logger = logger,
        ).toReviewDto()
    }

    override suspend fun getTvShowTrailer(tvShowId: Long): String {
        return handleRequest<TVShowVideosResponse>(
            apiCall = { tvShowApiService.getTvShowTrailer(tvShowId = tvShowId) },
            logger = logger
        ).mapToYoutubeURL()
    }

    override suspend fun getTopRatedTvShows(page: Int): PagedResultDto<TvShowDto> {
        return handleRequest<TopRatedTvShowSearchResponse>(
            apiCall = {
                tvShowApiService.getTopRatedTvShows(
                    page,
                    sortBy = "vote_average.desc",
                    minVoteCount = 200,
                )
            },
            logger = logger,
        ).toPagedTvShowDtos()
    }

    override suspend fun getTrendingTvShows(page: Int): PagedResultDto<TvShowDto> {
        return handleRequest<TrendingTvShowsResponse>(
            apiCall = { tvShowApiService.getTrendingTvShows(page) },
            logger = logger,
        ).toPagedTvShowDtos()
    }

    override suspend fun addTvShowRate(
        tvShowId: Long,
        rating: Int,
    ) {
        handleRequest<RatingResponse>(
            apiCall = {
                tvShowApiService.addTvShowRate(
                    seriesId = tvShowId,
                    rating = RatingRequest(rating)
                )
            },
            logger = logger
        )
    }

    override suspend fun deleteTvShowRate(
        tvShowId: Long
    ) {
        handleRequest<RatingResponse>(
            apiCall = {
                tvShowApiService.deleteTvShowRate(
                    tvShowId,
                )
            },
            logger = logger
        )
    }

    override suspend fun getTvShowAccountStates(
        tvShowId: Long
    ): MediaAccountStateDto {
        return handleRequest<MediaAccountStatesResponse>(
            apiCall = { tvShowApiService.getTvShowAccountStates(tvShowId) },
            logger = logger
        ).toDto()
    }

    override suspend fun getPopularTvShows(): List<TvShowDto> {
        return handleRequest<PopularTvShowsResponse>(
            apiCall = { tvShowApiService.getPopularTvShows() },
            logger = logger
        ).toTvShowDtos()
    }

    override suspend fun getUserRatedTvShows(
        accountId: Long,
        page: Int
    ): PagedResultDto<TvShowDto> {
        return handleRequest<MyRatingTvShowResponse>(
            apiCall = { tvShowApiService.getUserRatedTvShows(accountId, page) },
            logger = logger
        ).toPagedTvShowDtos()
    }
}