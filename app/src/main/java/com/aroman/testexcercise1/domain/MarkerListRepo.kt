package com.aroman.testexcercise1.domain

import com.aroman.testexcercise1.domain.entities.MarkerEntity

interface MarkerListRepo {
    suspend fun loadMarkerList(): List<MarkerEntity>
    suspend fun updateMarker(marker: MarkerEntity)
    suspend fun deleteMarker(marker: MarkerEntity)
}