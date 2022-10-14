package com.aroman.testexcercise1.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RoomConst.TABLE_MARKERS)
data class MarkerRoomEntity(
    @PrimaryKey(autoGenerate = true) val markerId: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val annotation: String
)