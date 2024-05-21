package com.example.devrev.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val adult: Boolean? = false,
    val backdrop_path: String? = null,
    val genre_ids: ArrayList<Int>? = ArrayList(),
    val id: Int = 0,
    val original_language: String?= null,
    val original_title: String?= null,
    val overview: String?= null,
    val popularity: Double? = 0.0,
    val poster_path: String?= null,
    val release_date: String?= null,
    val title: String?= null,
    val video: Boolean? = false,
    val vote_average: Double?=0.0,
    val vote_count: Int? = 0
):Parcelable