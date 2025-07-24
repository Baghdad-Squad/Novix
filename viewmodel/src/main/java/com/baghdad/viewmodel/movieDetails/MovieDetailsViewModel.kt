package com.baghdad.viewmodel.movieDetails

import com.baghdad.domain.usecase.movie.GetMovieCastMembersUseCase
import com.baghdad.domain.usecase.movie.GetMovieCategoryUseCase
import com.baghdad.domain.usecase.movie.GetMovieDetailsUseCase
import com.baghdad.domain.usecase.movie.GetMovieGalleryUseCase
import com.baghdad.domain.usecase.movie.GetSimilarMoviesUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.util.toDDMMYYYYFormat
import kotlin.math.roundToInt

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCastsInfoUseCase: GetMovieCastMembersUseCase,
    private val getMovieImagesUseCase: GetMovieGalleryUseCase,
    private val getMovieCategoryUseCase: GetMovieCategoryUseCase,
    private val getMoreLikeThisPosterImageUseCase: GetSimilarMoviesUseCase,
    private val movieId: Long,
) : BaseViewModel<MovieDetailsState, MovieDetailsEffect>(MovieDetailsState()),
    MovieDetailsInteractionListener {

    init {
        getMovieGallery()
        getMovieDetails()
        getCastMembers()
        getMoreLikeThisShow()
        getCastMembers()
    }

    override fun onStarMovieClick() {
        tryToExecute(
            callee = { currentState.movieId },
            onSuccess = {
                updateState {
                    it.copy(
                        isStared = !currentState.isStared,
                        isLoading = false,
                    )
                }
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }


    override fun onSaveCurrentMovieClick() {
        tryToExecute(
            callee = { currentState.movieId },
            onSuccess = {
                updateState {

                    it.copy(
                        isSaved = !currentState.isSaved,
                        isLoading = false
                    )
                }
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    override fun onSaveMoreLikeThisMedia(id: Long) {
        tryToExecute(
            callee = { currentState.moreLikeThisMovie.firstOrNull { it.id == id }?.id ?: 1L },
            onSuccess = ::onSaveMoreLikeThisMediaSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onSaveMoreLikeThisMediaSuccess(id: Long) {
        updateState { state ->
            val updatedMovies = state.moreLikeThisMovie.map {
                if (it.id == id) {
                    it.copy(isSaved = !it.isSaved)
                } else {
                    it
                }
            }
            state.copy(
                moreLikeThisMovie = updatedMovies,
                isLoading = false
            )
        }
    }

    override fun onExtendOverviewClick() {
        updateState { state ->
            state.copy(
                isExtendText = !state.isExtendText
            )
        }
    }

    override fun onCategoryClick(categoryId: Long) {
        sendEffect(MovieDetailsEffect.NavigateToCategory(categoryId))
    }

    override fun onBackClicked() {
        sendEffect(MovieDetailsEffect.NavigateBack)
    }


    override fun onActorClick(id: Long) {
        sendEffect(MovieDetailsEffect.NavigateToActorDetails(id))
    }

    override fun onReviewClick(id: Long) {
        sendEffect(MovieDetailsEffect.NavigateToReviewDetails(id))
    }

    override fun onMovieClick(id: Long) {
        sendEffect(MovieDetailsEffect.NavigateToMovie(id))
    }

    override fun onBackClick() {
        sendEffect(MovieDetailsEffect.NavigateBack)
    }

    override fun onTrailerClick() {
        sendEffect(MovieDetailsEffect.OpenYoutubeLink(currentState.movieTrailerURL))
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }


    private fun getMovieGallery() {
        tryToExecute(
            callee = {
                getMovieImagesUseCase(movieId = movieId)
            },
            onSuccess = ::onGetMovieGallerySuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetMovieGallerySuccess(images: List<String>) {
        updateState { state ->
            state.copy(
                movieImages = images,
                isLoading = false
            )
        }
    }

    private fun getMovieDetails() {
        tryToExecute(
            callee = { getMovieDetailsUseCase(movieId) },
            onSuccess = ::onGetMovieDetailsSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetMovieDetailsSuccess(details: Movie) {
        updateState { state ->
            state.copy(
                movieId = details.id,
                movieName = details.title,
                movieTrailerURL = details.trailerURL,
                overView = details.overview,
                rating = details.averageRating.roundToFirstDecimal(),
                duration = details.runtimeMinutes.formatDuration(),
                date = details.releaseDate.toDDMMYYYYFormat(),
                isSaved = state.isSaved,
                isLoading = false,
                categories = details.genres.map {
                    MovieDetailsState.CategoryUiState(
                        id = it.id,
                        name = it.name
                    )
                }
            )
        }
    }

    private fun getCastMembers() {
        tryToExecute(
            callee = { getCastsInfoUseCase(movieId) },
            onSuccess = { actors ->
                onGetMovieCastSuccess(
                    actors = actors.map { actor ->
                        MovieDetailsState.ActorCardInfo(
                            name = actor.actor.name,
                            imageUrl = actor.actor.profilePictureURL,
                            characterName = actor.characterName,
                            id = actor.actor.id.toInt()
                        )
                    }
                )
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun getMoreLikeThisShow() {
        tryToExecute(
            callee = { getMoreLikeThisPosterImageUseCase(movieId) },
            onSuccess = ::onGetMovieMoreLikeThisSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetMovieMoreLikeThisSuccess(movies: List<Movie>) {
        updateState { state ->
            state.copy(
                moreLikeThisMovie = movies.map { movie ->
                    MovieDetailsState.MoreLikeThisMovie(
                        imageUrl = movie.posterImageURL,
                        id = movie.id,
                        isSaved = false
                    )
                },
                isLoading = false
            )
        }
    }

    private fun onGetMovieCastSuccess(actors: List<MovieDetailsState.ActorCardInfo>) {
        updateState { state ->
            state.copy(
                castMembers = actors,
                isLoading = false
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}

private fun Double.roundToFirstDecimal(): Double {
    return (this * 10).roundToInt() / 10.0
}

private fun Int.formatDuration(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        this <= 0 -> "unknown"
        else -> if (hours > 0) {
            "$hours hr $minutes min"
        } else {
            "$minutes min"
        }
    }

}
