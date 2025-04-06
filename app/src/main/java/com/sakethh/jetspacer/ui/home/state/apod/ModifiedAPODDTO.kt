package com.sakethh.jetspacer.ui.home.state.apod

import kotlinx.serialization.Serializable


@Serializable
data class ModifiedAPODDTO(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val title: String,
    val url: String,
    val apodWebPageURL: String = try {
        "https://apod.nasa.gov/apod/ap${
            date.substringBefore("-").substring(2)
        }${date.split("-")[1].substringBefore("-")}${
            date.split("-")[2].substringBefore(
                "-"
            )
        }.html"
    } catch (e: Exception) {
        e.printStackTrace()
        url
    }
)
