package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto

@kotlinx.serialization.Serializable
data class IPGeoLocationDTO(
    val current_time: String?="",
    val date: String?="",
    val day_length: String?="",
    val location: Location?=null,
    val moon_altitude: Double?=null,
    val moon_azimuth: Double?=null,
    val moon_distance: Double?=null,
    val moon_parallactic_angle: Double?=null,
    val moon_status: String?="",
    val moonrise: String?="",
    val moonset: String?="",
    val solar_noon: String?="",
    val sun_altitude: Double?=null,
    val sun_azimuth: Double?=null,
    val sun_distance: Double?=null,
    val sun_status: String?="",
    val sunrise: String?="",
    val sunset: String?=""
)