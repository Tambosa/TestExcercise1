package com.aroman.testexcercise1.data

import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity

class FakeMarkerListRepoImpl() : MarkerListRepo {
    private val list = arrayListOf<MarkerEntity>(
        MarkerEntity(0, "Sydney", -34.0, 151.0, "Largest city in Australia"),
        MarkerEntity(1, "London", 51.509865, -0.118092, "Capital of UK"),
        MarkerEntity(2, "Moscow", 55.751244, 37.618423, "Capital of Russia"),
    )

    override suspend fun loadMarkerList(): List<MarkerEntity> {
        return list
    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        //nothing
    }

    override suspend fun deleteMarker(marker: MarkerEntity) {
        list.remove(marker)
    }
}