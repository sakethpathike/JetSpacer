package com.sakethh.jetspacer.screens.space.remote.data.marsWeather.dto

import kotlinx.serialization.Contextual

@kotlinx.serialization.Serializable
data class MarsWeatherDTO(
    val TZ_Data: String = "",
    val atmo_opacity: String = "",
    val id: Int = 0,
    val local_uv_irradiance_index: String = "",
    val ls: Int = 0,
    val max_gts_temp: Int = 0,
    val max_temp: Int = 0,
    val min_gts_temp: Int = 0,
    val min_temp: Int = 0,
    val pressure: Int = 0,
    val pressure_string: String = "",
    val season: String = "",
    val sol: Int = 0,
    val status: Int = 0,
    val sunrise: String = "",
    val sunset: String = "",
    val terrestrial_date: String = "",
    val unitOfMeasure: String = ""
)