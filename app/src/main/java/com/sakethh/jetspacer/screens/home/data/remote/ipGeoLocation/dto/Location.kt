package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto

@kotlinx.serialization.Serializable
data class Location(
    val city: String?="",
    val country_code2: String?="",
    val country_code3: String?="",
    val country_name: String?="",
    val district: String?="",
    val ip: String?="",
    val latitude: Double?=null,
    val longitude: Double?=null,
    val state_prov: String?="",
    val zipcode: String?=""
)