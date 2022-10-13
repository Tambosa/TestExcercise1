package com.aroman.testexcercise1.di

import com.aroman.testexcercise1.data.FakeMarkerListRepoImpl
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.ui.favourites.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val repository = module {
//        single<RoomDb> {
//            Room.databaseBuilder(
//                androidApplication(),
//                RoomDb::class.java,
//                RoomConst.DB_NAME
//            ).build()
//        }
        single<MarkerListRepo> { FakeMarkerListRepoImpl() }
    }

    val viewModel = module {
        viewModel { FavouritesViewModel(get<MarkerListRepo>()) }
    }
}