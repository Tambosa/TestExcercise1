package com.aroman.testexcercise1.data.room

import androidx.room.*

@Dao
interface MarkerDAO {
    @Query("SELECT * FROM ${RoomConst.TABLE_MARKERS}")
    fun getAllMarkers(): List<MarkerRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewMarker(marker: MarkerRoomEntity)

    @Update
    fun updateMaterial(marker: MarkerRoomEntity)

    @Delete
    fun deleteMaterial(marker: MarkerRoomEntity)
}