package com.baghdad.novix.di

import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.genre.GetGenreMoviesUseCase
import com.baghdad.domain.usecase.genre.GetGenreTvShowsUseCase
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMovieCastMembersUseCase
import com.baghdad.domain.usecase.movie.GetMovieCategoryUseCase
import com.baghdad.domain.usecase.movie.GetMovieDetailsUseCase
import com.baghdad.domain.usecase.movie.GetMovieGalleryUseCase
import com.baghdad.domain.usecase.movie.GetSimilarMoviesUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowImagesUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetGenresUseCase)
    singleOf(::AddRecentlyViewedUseCase)
    singleOf(::DeleteAllRecentlyViewedUseCase)
    singleOf(::GetRecentlyViewedUseCase)
    singleOf(::DeleteAllRecentSearchesUseCase)
    singleOf(::DeleteRecentSearchUseCase)
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::SearchUseCase)
    singleOf(::GetActorInfoUseCase)
    singleOf(::GetActorMoviesUseCase)
    singleOf(::GetActorTvShowUseCase)
    singleOf(::GetActorGalleryUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMovieCastMembersUseCase)
    singleOf(::GetSimilarMoviesUseCase)
    singleOf(::GetTvShowDetailsUseCase)
    singleOf(::GetTvShowCastMembersUseCase)
    singleOf(::GetTvShowImagesUseCase)
    singleOf(::GetTvShowsByGenreUseCase)
    singleOf(::GetTvShowSeasonEpisodesUseCase)
    singleOf(::GetEpisodeDetailsUseCase)
    singleOf(::GetEpisodeCastMembersUseCase)
    singleOf(::GetMovieReviewsUseCase)
    singleOf(::GetTvShowReviewsUseCase)
    singleOf(::GetMovieCategoryUseCase)
    singleOf(::SearchMoviesUseCase)
    singleOf(::SearchTvShowsUseCase)
    singleOf(::SearchActorsUseCase)
    singleOf(::GetMovieGalleryUseCase)
    singleOf(::GetGenreTvShowsUseCase)
    singleOf(::GetGenreMoviesUseCase)
    singleOf(::GetTvShowGenreNameByIdUseCase)
    singleOf(::GetMovieGenreNameByIdUseCase)
}

