package com.aroman.testexcercise1.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MarkerRoomEntity::class], version = 1, exportSchema = false
)

abstract class RoomDb : RoomDatabase() {
    abstract fun MarkerDAO(): MarkerDAO
}