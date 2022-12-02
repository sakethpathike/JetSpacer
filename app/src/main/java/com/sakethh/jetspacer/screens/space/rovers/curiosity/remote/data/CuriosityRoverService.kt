package com.sakethh.jetspacer.screens.space.rovers.curiosity.remote.data

import com.sakethh.jetspacer.screens.space.rovers.curiosity.remote.data.dto.CuriosityRoverDTO

interface CuriosityRoverService {
    suspend fun getRandomImages(sol: Int): CuriosityRoverDTO
}