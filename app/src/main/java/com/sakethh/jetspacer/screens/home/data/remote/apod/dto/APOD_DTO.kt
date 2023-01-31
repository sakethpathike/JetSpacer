package com.sakethh.jetspacer.screens.home.data.remote.apod.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class APOD_DTO(
    @SerialName("date")
    val date: String?="",
    @SerialName("explanation")
    val explanation: String?="",
    @SerialName("hdurl")
    val hdurl: String?="",
    @SerialName("media_type")
    val media_type: String?="",
    @SerialName("service_version")
    val service_version: String?="",
    @SerialName("title")
    val title: String?="",
    @SerialName("url")
    val url: String?="",
    @SerialName("copyright")
    val copyright:String?=""
)
