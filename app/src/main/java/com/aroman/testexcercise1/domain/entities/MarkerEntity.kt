package com.aroman.testexcercise1.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarkerEntity(
    var id: Int = 0,
    var name: String,
    var lat: Double,
    var long: Double,
    var annotation: String,
) : Parcelable