package com.sakethh.jetspacer.home.presentation.state.apod


data class ModifiedAPODDTO(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val title: String,
    val url: String
)
