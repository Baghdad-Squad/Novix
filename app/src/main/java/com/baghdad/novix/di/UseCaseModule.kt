package com.baghdad.novix.di

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchUseCase
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
}

