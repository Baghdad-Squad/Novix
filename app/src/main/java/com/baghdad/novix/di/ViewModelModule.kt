package com.baghdad.novix.di

import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import com.baghdad.viewmodel.review.ContentType
import com.baghdad.viewmodel.review.ReviewViewModel
import com.baghdad.viewmodel.search.SearchViewModel
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (id: Long) ->
        MovieDetailsViewModel(
            movieId = id,
            getMovieDetailsUseCase = get(),
            getCastsInfoUseCase = get(),
            getMovieImagesUseCase = get(),
            getMoreLikeThisPosterImageUseCase = get(),
            getMovieCategoryUseCase = get()
        )
    }

    viewModel {
        (actorId: Long) -> ActorGalleryViewModel(get(), actorId)
    }
    viewModel { (actorId: Long) ->
        ActorDetailsViewModel(actorId, get(), get(), get(), get())

    }
    viewModel { (mediaId: Long, mediaType: ContentType) ->
        ReviewViewModel(
            contentId = mediaId,
            contentType = mediaType,
            getMovieReviewsUseCase = get(),
            getSeriesReviewsUseCase = get()
        )
    }

    viewModelOf(::EpisodeDetailsViewModel)
    viewModel{ (actorId: Long) ->
        TopMoviePicksViewModel(actorId, get())
    }
    viewModel { (actorId: Long) ->
        TopTvShowViewModel(actorId, get())
    }
}