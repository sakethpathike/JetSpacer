package com.sakethh.jetspacer.screens.home.data.remote.apod

import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO

interface APODService {
    suspend fun getAPOD(): APOD_DTO
}