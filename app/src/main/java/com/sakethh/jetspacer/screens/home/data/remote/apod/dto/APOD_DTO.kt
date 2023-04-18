package com.sakethh.jetspacer.screens.home.data.remote.apod.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class APOD_DTO(
    @SerialName("date")
    var date: String?="",
    @SerialName("explanation")
    var explanation: String?="",
    @SerialName("hdurl")
    var hdurl: String?="",
    @SerialName("media_type")
    var media_type: String?="",
    @SerialName("service_version")
    var service_version: String?="",
    @SerialName("title")
    var title: String?="",
    @SerialName("url")
    var url: String?="",
    @SerialName("copyright")
    var copyright:String?=""
)
