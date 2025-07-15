package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiState

data class MovieDetailsState(
    val movieId: Long = 0L,
    val movieImages: List<String> = listOf(
        "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
        "https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
        "https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
        "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg"


    ),
    val movieName: String = "The Wolf of Wall Street",
    val categories: List<String> = listOf(
        "Crime",
        "Drama",
        "Biography",
        "Comedy"
    ),
    val rating: Double = 8.2,
    val duration: String = "3h 0m",
    val date: String = "2013-12-25",
    val overView: String = "The Wolf of Wall Street is a 2013 American biographical black comedy crime film directed by Martin Scorsese, based on the memoir of the same name by Jordan Belfort. The film stars Leonardo DiCaprio as Belfort, a stockbroker who engages in corruption and fraud on Wall Street in the 1990s.",
    val castes: List<ActorCardInfo> = emptyList() ,
    val moreLikeThisMovie: List<MoreLikeThisMovie> = listOf(
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            id = 1,
            isSaved = false
        ),
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
            id = 2,
            isSaved = false
        ),
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
            id = 3,
            isSaved = false
        ),
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg",
            id = 4,
            isSaved = false
        ),
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/6DrHO1jr3qVrViUO6s6kFiAGM7.jpg",
            id = 5,
            isSaved = false
        ),
        MoreLikeThisMovie(
            imageUrl = "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
            id = 6,
            isSaved = false
        ),
    ),
    val isExtendText: Boolean = false,
    val isStared: Boolean = false,
    val isSaved: Boolean = false,
    val isHasTrailer: Boolean = true,
    override val isLoading: Boolean = false,
) : BaseUiState {

    data class ActorCardInfo(
        val name: String = "",
        val imageUrl: String? = null,
        val characterName: String = "",
        val id: Int = 0,
    )

    data class MoreLikeThisMovie(
        val imageUrl: String = "",
        val id: Long = 0,
        val isSaved: Boolean = false,
    )
}