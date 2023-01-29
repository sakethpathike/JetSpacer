package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation

import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO

interface IPGeolocationService {
    suspend fun getGeoLocationData(): IPGeoLocationDTO
}