package com.sakethh.jetspacer.screens.space.remote.data.marsWeather.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MarsWeatherDTO(
    @SerialName("TZ_Data")
    val TZ_Data: String = "",
    @SerialName("atmo_opacity")
    val atmo_opacity: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("local_uv_irradiance_index")
    val local_uv_irradiance_index: String = "",
    @SerialName("ls")
    val ls: Int = 0,
    @SerialName("max_gts_temp")
    val max_gts_temp: Int = 0,
    @SerialName("max_temp")
    val max_temp: Int = 0,
    @SerialName("min_gts_temp")
    val min_gts_temp: Int = 0,
    @SerialName("min_temp")
    val min_temp: Int = 0,
    @SerialName("pressure")
    val pressure: Int = 0,
    @SerialName("pressure_string")
    val pressure_string: String = "",
    @SerialName("season")
    val season: String = "",
    @SerialName("sol")
    val sol: Int = 0,
    @SerialName("status")
    val status: Int = 0,
    @SerialName("sunrise")
    val sunrise: String = "",
    @SerialName("sunset")
    val sunset: String = "",
    @SerialName("terrestrial_date")
    val terrestrial_date: String = "",
    @SerialName("unitOfMeasure")
    val unitOfMeasure: String = ""
)