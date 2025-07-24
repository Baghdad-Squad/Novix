package com.baghdad.novix.di

import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesViewModel
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsViewModel
import com.baghdad.viewmodel.continueWatching.ContinueWatchingViewModel
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import com.baghdad.viewmodel.login.LoginViewModel
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import com.baghdad.viewmodel.trendingActors.TrendingActorViewModel
import com.baghdad.viewmodel.review.ContentType
import com.baghdad.viewmodel.review.ReviewViewModel
import com.baghdad.viewmodel.search.SearchViewModel
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel
import com.baghdad.viewmodel.topRating.TopRatingViewModel
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowViewModel
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    single { SearchFilterHelper() }
    viewModel { (actorId: Long) ->
        ActorGalleryViewModel(get(), actorId)
    }
    viewModel { (id: Long) ->
        MovieDetailsViewModel(
            movieId = id,
            getMovieDetailsUseCase = get(),
            getCastsInfoUseCase = get(),
            getMovieImagesUseCase = get(),
            getMoreLikeThisPosterImageUseCase = get(),
            addContinueWatchingUseCase = get()
        )
    }

    viewModel { (actorId: Long) ->
        ActorGalleryViewModel(get(), actorId)
    }
    viewModel { (actorId: Long) ->
        ActorDetailsViewModel(actorId, get(), get(), get(), get())
    }
    viewModel { (tvShowId: Long) ->
        TvShowDetailsViewModel(tvShowId, get(), get(), get())
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
    viewModel { (actorId: Long) ->
        TopMoviePicksViewModel(actorId, get())
    }
    viewModel { (actorId: Long) ->
        TopTvShowViewModel(actorId, get())
    }
    viewModel { (tvShowId: Long, seasonNumber: Int, episodeNumber: Int) ->
        EpisodeDetailsViewModel(tvShowId, seasonNumber, episodeNumber, get(), get())
    }
    viewModel { (categoryId: Long) ->
        CategoryTvShowsViewModel(categoryId, get(), get())
    }

    viewModel { (categoryId: Long) ->
        CategoryMoviesViewModel(categoryId, get(), get())
    }

    viewModelOf(::TopRatingViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ContinueWatchingViewModel)
    viewModelOf(::TrendingActorViewModel)
}
