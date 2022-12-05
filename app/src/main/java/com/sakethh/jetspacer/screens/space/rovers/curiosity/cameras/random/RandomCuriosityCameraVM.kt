package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.RandomCameraCuriosityFetching
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.RandomCameraCuriosityImpl
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Camera
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Rover
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RandomCuriosityCameraVM(private val randomCameraCuriosityFetching: RandomCameraCuriosityFetching = RandomCameraCuriosityFetching()) :
    ViewModel() {
    private val _randomCuriosityCameraData = MutableStateFlow<List<Photo>>(emptyList())
    val randomCuriosityCameraData = _randomCuriosityCameraData.asStateFlow()
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val enteredSol = mutableStateOf<String?>("0")
    val currentPage = mutableStateOf(1)

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            getRandomCuriosityData(sol = 0, 1)
        }
    }

    suspend fun getRandomCuriosityData(sol: Int, page: Int) {
        _randomCuriosityCameraData.emit(
            randomCameraCuriosityFetching.getRandomCuriosityData(
                sol,
                page
            )
        )
    }
}