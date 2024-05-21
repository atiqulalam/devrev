package com.example.devrev.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpokenLanguage(
    val english_name: String?,
    val iso_639_1: String?,
    val name: String?
):Parcelable