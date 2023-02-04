package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class Location(
    @SerialName("city")
    val city: String?="",
    @SerialName("country_code2")
    val country_code2: String?="",
    @SerialName("country_code3")
    val country_code3: String?="",
    @SerialName("country_name")
    val country_name: String?="",
    @SerialName("district")
    val district: String?="",
    @SerialName("ip")
    val ip: String?="",
    @SerialName("latitude")
    val latitude: Double?=null,
    @SerialName("longitude")
    val longitude: Double?=null,
    @SerialName("state_prov")
    val state_prov: String?="",
    @SerialName("zipcode")
    val zipcode: String?=""
)