package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IPGeoLocationDTO(
    @SerialName("current_time")
    val current_time: String?="",
    @SerialName("date")
    val date: String?="",
    @SerialName("day_length")
    val day_length: String?="",
    @SerialName("location")
    val location: Location?=null,
    @SerialName("moon_altitude")
    val moon_altitude: Double?=null,
    @SerialName("moon_azimuth")
    val moon_azimuth: Double?=null,
    @SerialName("moon_distance")
    val moon_distance: Double?=null,
    @SerialName("moon_parallactic_angle")
    val moon_parallactic_angle: Double?=null,
    @SerialName("moon_status")
    val moon_status: String?="",
    @SerialName("moonrise")
    val moonrise: String?="",
    @SerialName("moonset")
    val moonset: String?="",
    @SerialName("solar_noon")
    val solar_noon: String?="",
    @SerialName("sun_altitude")
    val sun_altitude: Double?=null,
    @SerialName("sun_azimuth")
    val sun_azimuth: Double?=null,
    @SerialName("sun_distance")
    val sun_distance: Double?=null,
    @SerialName("sun_status")
    val sun_status: String?="",
    @SerialName("sunrise")
    val sunrise: String?="",
    @SerialName("sunset")
    val sunset: String?=""
)