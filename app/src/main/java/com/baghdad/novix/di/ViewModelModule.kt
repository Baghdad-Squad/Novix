package com.baghdad.novix.di

import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesViewModel
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsViewModel
import com.baghdad.viewmodel.continueWatching.ContinueWatchingViewModel
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import com.baghdad.viewmodel.home.HomeViewModel
import com.baghdad.viewmodel.login.LoginViewModel
import com.baghdad.viewmodel.main.MainViewModel
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import com.baghdad.viewmodel.review.ContentType
import com.baghdad.viewmodel.review.ReviewViewModel
import com.baghdad.viewmodel.search.SearchViewModel
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel
import com.baghdad.viewmodel.topRating.TopRatingViewModel
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksViewModel
import com.baghdad.viewmodel.trendingActors.TrendingActorViewModel
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesViewModel
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowViewModel
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
            addContinueWatchingUseCase = get(),
            ioDispatcher = get(),
        )
    }

    viewModel { (actorId: Long) ->
        ActorGalleryViewModel(get(), actorId, get())
    }
    viewModel { (actorId: Long) ->
        ActorDetailsViewModel(
            actorId,
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel{(tvShowId: Long) ->
        TvShowDetailsViewModel(
            tvShowId = tvShowId,
            getTvShowDetailsUseCase = get(),
            getTvShowCastMembersUseCase = get(),
            getTvShowSeasonEpisodesUseCase = get(),
            addContinueWatchingUseCase = get(),
            ioDispatcher = get()
        )
    }

    viewModel { (mediaId: Long, mediaType: ContentType) ->
        ReviewViewModel(
            contentId = mediaId,
            contentType = mediaType,
            getMovieReviewsUseCase = get(),
            getSeriesReviewsUseCase = get(),
        )
    }

    viewModelOf(::EpisodeDetailsViewModel)
    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModel { (actorId: Long) ->
        TopMoviePicksViewModel(actorId, get(), get())
    }
    viewModel { (actorId: Long) ->
        TopTvShowPicksViewModel(actorId, get(), get())
    }
    viewModel {
        SearchViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
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
    viewModel {
        MainViewModel(
            isLoggedInUseCase = get()
        )
    }
    single<CoroutineDispatcher> { Dispatchers.IO }

    viewModelOf(::TopRatingViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ContinueWatchingViewModel)
    viewModelOf(::TrendingActorViewModel)
    viewModelOf(::TrendingTvShowViewModel)
    viewModelOf(::TrendingMoviesViewModel)
    viewModelOf(::HomeViewModel)
}
