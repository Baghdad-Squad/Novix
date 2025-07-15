package com.baghdad.novix.di

import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (actorId: Long) ->
        ActorDetailsViewModel(actorId, get(), get(), get(), get())
    }
}