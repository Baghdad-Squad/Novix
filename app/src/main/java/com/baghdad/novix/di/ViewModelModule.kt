package com.baghdad.novix.di

import com.baghdad.viewmodel.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(
            getGenresUseCase = get(),
            getRecentSearchesUseCase = get(),
            deleteAllRecentlyViewedUseCase = get(),
            deleteAllRecentSearchesUseCase = get(),
            deleteRecentSearchUseCase = get(),
            searchUseCase = get()
        )
    }
}