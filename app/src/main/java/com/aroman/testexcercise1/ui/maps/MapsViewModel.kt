package com.aroman.testexcercise1.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import kotlinx.coroutines.*

class MapsViewModel(private val repo: MarkerListRepo) : ViewModel() {
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

    fun insertMarker(marker: MarkerEntity) {
        viewModelCoroutineScope.launch { susInsertMarker(marker) }
    }

    private suspend fun susInsertMarker(marker: MarkerEntity) {
        withContext(Dispatchers.IO) {
            repo.insertMarker(marker)
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