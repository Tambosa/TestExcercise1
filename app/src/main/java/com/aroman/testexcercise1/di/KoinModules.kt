package com.aroman.testexcercise1.di

import androidx.room.Room
import com.aroman.testexcercise1.data.RoomMarkerListRepoImpl
import com.aroman.testexcercise1.data.room.MarkerDAO
import com.aroman.testexcercise1.data.room.RoomConst
import com.aroman.testexcercise1.data.room.RoomDb
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.ui.maps.MapsViewModel
import com.aroman.testexcercise1.ui.favourites.FavouritesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val repository = module {
        single<RoomDb> {
            Room.databaseBuilder(
                androidApplication(),
                RoomDb::class.java,
                RoomConst.DB_NAME
            ).build()
        }

        single<MarkerDAO> {
            get<RoomDb>().MarkerDAO()
        }

        single<MarkerListRepo> { RoomMarkerListRepoImpl(get<MarkerDAO>()) }
    }

    val viewModel = module {
        viewModel { FavouritesViewModel(get<MarkerListRepo>()) }
        viewModel { MapsViewModel(get<MarkerListRepo>()) }
    }
}