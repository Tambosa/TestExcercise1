package com.aroman.testexcercise1.data

import com.aroman.testexcercise1.data.room.MarkerDAO
import com.aroman.testexcercise1.data.room.MarkerMapper
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity

class RoomMarkerListRepoImpl(private val dao: MarkerDAO) : MarkerListRepo {
    override suspend fun loadMarkerList(): List<MarkerEntity> {
        val tempList = dao.getAllMarkers()
        val returnList = mutableListOf<MarkerEntity>()
        for (markerEntityRoom in tempList) {
            returnList.add(
                MarkerMapper().fromModel(markerEntityRoom)
            )
        }
        return returnList
    }

    override suspend fun insertMarker(marker: MarkerEntity) {
        dao.insertNewMarker(MarkerMapper().toModel(marker))
    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        dao.updateMaterial(MarkerMapper().toModel(marker))
    }

    override suspend fun deleteMarker(marker: MarkerEntity) {
        dao.deleteMaterial(MarkerMapper().toModel(marker))
    }
}