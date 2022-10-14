package com.aroman.testexcercise1.ui.markerDetails

import androidx.lifecycle.ViewModel
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import kotlinx.coroutines.*

class MarkerDetailsViewModel(private val repo: MarkerListRepo) : ViewModel() {

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