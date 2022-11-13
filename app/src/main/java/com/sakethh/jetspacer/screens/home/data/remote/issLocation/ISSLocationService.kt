package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO

interface ISSLocationService {
    suspend fun getISSLocation():ISSLocationDTO
}