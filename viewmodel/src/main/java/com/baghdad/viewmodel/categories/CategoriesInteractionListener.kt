package com.baghdad.viewmodel.categories

interface CategoriesInteractionListener {
   fun onCategoryMovieClick(categoryId: Long)
   fun onCategoryTvShowClick(categoryId: Long)

   fun onTabSelected(tab: CategoriesState.CategoriesTab)

   fun onSnackBarActionLabelClicked()

}