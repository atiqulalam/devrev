package com.example.devrev.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
):Parcelable