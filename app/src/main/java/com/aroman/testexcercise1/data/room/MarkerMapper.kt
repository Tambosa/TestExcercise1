package com.aroman.testexcercise1.data.room

import com.aroman.testexcercise1.domain.DomainToModelMapper
import com.aroman.testexcercise1.domain.entities.MarkerEntity

class MarkerMapper : DomainToModelMapper<MarkerEntity, MarkerRoomEntity> {
    override fun toModel(marker: MarkerEntity): MarkerRoomEntity {
        return MarkerRoomEntity(
            markerId = marker.id,
            name = marker.name,
            latitude = marker.lat,
            longitude = marker.long,
            annotation = marker.annotation
        )
    }

    override fun fromModel(marker: MarkerRoomEntity): MarkerEntity {
        return MarkerEntity(
            id = marker.markerId,
            name = marker.name,
            lat = marker.latitude,
            long = marker.longitude,
            annotation = marker.annotation
        )
    }
}