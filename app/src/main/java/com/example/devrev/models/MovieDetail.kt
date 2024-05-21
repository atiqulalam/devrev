package com.example.devrev.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    val adult: Boolean? = false,
    val backdrop_path: String? = null,
    val belongs_to_collection: BelongsToCollection? = null,
    val budget: Int? = 0,
    val genres: ArrayList<Genre>? = ArrayList(),
    val homepage: String? = null,
    val id: Int? = 0,
    val imdb_id: String? = null,
    val origin_country: ArrayList<String>? = ArrayList(),
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val production_companies: ArrayList<ProductionCompany>? = ArrayList(),
    val production_countries: ArrayList<ProductionCountry>? = ArrayList(),
    val release_date: String? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val spoken_languages: List<SpokenLanguage>? = ArrayList(),
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = false,
    val vote_average: Double? = null,
    val vote_count: Int? = 0
):Parcelable