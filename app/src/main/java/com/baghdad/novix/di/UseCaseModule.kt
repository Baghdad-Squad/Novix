package com.baghdad.novix.di

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetGenresUseCase(
            movieRepository = get(),
            tvShowRepository = get()
        )
    }
    single {
        GetRecentSearchesUseCase(
            searchRepository = get()
        )
    }
    single {
        DeleteAllRecentlyViewedUseCase(
            recentlyViewedRepository = get()
        )
    }
    single {
        DeleteAllRecentlyViewedUseCase(
            recentlyViewedRepository = get()
        )
    }
    single {
        DeleteAllRecentSearchesUseCase(
            searchRepository = get()
        )
    }
    single {
        DeleteRecentSearchUseCase(
            searchRepository = get()
        )
    }
    single {
        SearchUseCase(
            searchRepository = get()
        )
    }
}

