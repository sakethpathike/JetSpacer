package com.sakethh.jetspacer.screens.home.data.remote.apod.dto

@kotlinx.serialization.Serializable
data class APOD_DTO(
    val date: String?="",
    val explanation: String?="",
    val hdurl: String?="",
    val media_type: String?="",
    val service_version: String?="",
    val title: String?="",
    val url: String?="",
    val copyright:String?=""
)
