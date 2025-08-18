package com.sakethh.jetspacer.ui.screens.explore

data class ISSLocationState(
    val latitude: String,
    val longitude: String,
    val message: String,
    val timestamp: String,
    val error: Boolean,
    val errorMessage: String,
)