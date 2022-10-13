package com.aroman.testexcercise1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import kotlinx.coroutines.*

class FavouritesViewModel(private val repo: MarkerListRepo) : ViewModel() {
    private val _liveData: MutableLiveData<List<MarkerEntity>> = MutableLiveData()
    val markerList: LiveData<List<MarkerEntity>> = _liveData

    fun loadMarkerList() {
        viewModelCoroutineScope.launch { susLoadMarkerList() }
    }

    private suspend fun susLoadMarkerList() {
        withContext(Dispatchers.IO) {
            _liveData.postValue(repo.loadMarkerList())
        }
    }

    fun deleteMarker(marker: MarkerEntity) {
        viewModelCoroutineScope.launch { susDeleteMarker(marker) }
    }

    private suspend fun susDeleteMarker(marker: MarkerEntity) {
        withContext(Dispatchers.IO) {
            repo.deleteMarker(marker)
        }
    }

    fun updateMarker(marker: MarkerEntity) {
        viewModelCoroutineScope.launch { susUpdateMarker(marker) }
    }

    private suspend fun susUpdateMarker(marker: MarkerEntity) {
        withContext(Dispatchers.IO) {
            repo.updateMarker(marker)
        }
    }

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }
    )

    private fun handleError(error: Throwable) {
        //nothing
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancel()
    }
}
