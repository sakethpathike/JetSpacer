package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.RandomCameraCuriosityFetching
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo

class RandomCuriosityCameraVM(private val randomCameraCuriosityFetching: RandomCameraCuriosityFetching = RandomCameraCuriosityFetching()) :
    ViewModel() {
    val _randomCuriosityCameraData = mutableStateOf<List<Photo>>(emptyList())
    val randomCuriosityCameraData = mutableStateOf<List<Photo>>(emptyList())
    val isRandomCamerasDataLoaded = mutableStateOf(false)
    val currentPage = mutableStateOf(1)

    suspend fun getRandomCuriosityData(sol: Int, page: Int) {
        _randomCuriosityCameraData.value =
            randomCameraCuriosityFetching.getRandomCuriosityData(sol, page)
        randomCuriosityCameraData.value += _randomCuriosityCameraData.value
        isRandomCamerasDataLoaded.value = true
    }

    init {
        CurrentHTTPCodes.marsRoversDataHTTPCode.value=200
    }

    fun clearRandomCuriosityCameraData() {
        randomCuriosityCameraData.value = emptyList()
    }
}