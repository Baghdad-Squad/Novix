package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class MovieDetailsViewModel(
) : BaseViewModel<MovieDetailsState, MovieDetailsEffect>(MovieDetailsState()),
    MovieDetailsInteractionListener {

     init {
        onGetMovieDetails()
        onGetMovieActorsSuccess(currentState.castes)
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
                        isLoading = false)
                }
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    override fun onSaveMoreLikeThisMedia(id: Long) {
        tryToExecute(
            callee = { currentState.moreLikeThisMovie.firstOrNull { it.id == id } },
            onSuccess = { movie ->
                val updatedMovies = currentState.moreLikeThisMovie.map {
                    if (it.id == (movie?.id)) {
                        it.copy(isSaved = !it.isSaved)
                    } else {
                        it
                    }
                }
                updateState { state ->
                    state.copy(
                        moreLikeThisMovie = updatedMovies,
                        isLoading = false
                    )
                }
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    override fun onExtendOverviewClick() {
        updateState { state ->
            state.copy(
                isExtendText = !state.isExtendText
            )
        }
    }


    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }




    private fun onGetMovieDetailsSuccess(movieDetails: MovieDetailsState) {
        updateState { state ->
            state.copy(
                movieImages = movieDetails.movieImages,
                movieName = movieDetails.movieName,
                categories = movieDetails.categories,
                rating = movieDetails.rating,
                castes = listOf(
                    MovieDetailsState.ActorCardInfo(
                        name = "Leonardo DiCaprio",
                        "https://image.tmdb.org/t/p/w500/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg",
                        characterName = "Jordan Belfort",
                        id = 1
                    ),
                    MovieDetailsState.ActorCardInfo(
                        name = "The Rock ",
                        "https://image.tmdb.org/t/p/w500/kuqFzlYMc2IrsOyPznMd1FroeGq.jpg",
                        characterName = "Jordan Belfort",
                        id = 2
                    ),
                    MovieDetailsState.ActorCardInfo(
                        name = "Robert Downey Jr",
                        "https://image.tmdb.org/t/p/w500/im9SAqJPZKEbVZGmjXuLI4O7RvM.jpg",
                        characterName = "Jordan Belfort",
                        id = 2
                    ),
                ),
                duration = movieDetails.duration,
                date = movieDetails.date,

                isLoading = false
            )
        }
    }

    private fun onGetMovieActorsSuccess(actors: List<MovieDetailsState.ActorCardInfo>) {
        updateState { state ->
            state.copy(
                castes = actors,
                isLoading = false
            )
        }
    }

    private fun onGetMovieDetails(){
        tryToExecute(
            callee = { currentState },
            onSuccess = { movieDetails ->
                onGetMovieDetailsSuccess(movieDetails)
            },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}