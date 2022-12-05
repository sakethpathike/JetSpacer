package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RandomCameraCuriosityFetching(private val randomCameraCuriosityImpl: RandomCameraCuriosityImpl = RandomCameraCuriosityImpl()) {
    suspend fun getRandomCuriosityData(sol: Int, page: Int): List<Photo> {
        val imagesData = mutableStateListOf<Deferred<List<Photo>>>()
        coroutineScope {
            val _imagesData =
                async { randomCameraCuriosityImpl.getRandomCuriosityData(sol, page).photos }
            imagesData.add(_imagesData)
        }
        return imagesData.awaitAll().component1()
    }
}