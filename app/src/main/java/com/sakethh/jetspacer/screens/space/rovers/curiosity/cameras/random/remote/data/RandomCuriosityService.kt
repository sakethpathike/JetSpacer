package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data

import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO

interface RandomCuriosityService {
    suspend fun getRandomCuriosityData(sol: Int,page:Int): RandomCameraDTO
}