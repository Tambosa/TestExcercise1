package com.aroman.testexcercise1.domain.entities

data class MarkerEntity(
    var id: Int = 0,
    var name: String,
    var lat: Double,
    var long: Double,
    var annotation: String,
)