package com.baghdad.repository.model


data class TvShowDto(
    override val id: Long,
    override val title: String,
    override val genres: List<GenreDto>,
    override val imdbRating: Double,
    override val userRating: Double?,
    override val releaseDate: String,
    override val overview: String,
    override val cast: List<CastMemberDto>,
    override val posterPictureURL: String,
    override val backdropPicturesURLs: List<String>,
    val numberOfSeasons: Int
) : MediaDto {
    override val type: MediaType
        get() = MediaType.TV_SHOW
}
