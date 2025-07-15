package com.baghdad.novix.di

import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.search.SearchViewModel
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::ActorDetailsViewModel)
    viewModelOf(::TopMoviePicksViewModel)
}