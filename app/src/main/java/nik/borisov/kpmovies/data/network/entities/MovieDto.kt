package nik.borisov.kpmovies.data.network.entities

import com.google.gson.annotations.SerializedName

data class MovieDto(

    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("rating")
    val rating: RatingDto,
    @SerializedName("movieLength")
    val movieLength: Int,
    @SerializedName("poster")
    val poster: PosterDto,
    @SerializedName("genres")
    val genres: List<GenreDto>,
    @SerializedName("countries")
    val countries: List<CountryDto>,
    @SerializedName("videos")
    val trailersResponse: TrailersResponse
)
