package com.sakethh.jetspacer.home.settings.presentation.utils

import androidx.compose.runtime.mutableStateOf

object GlobalSettings {
    val isDarkModeEnabled = mutableStateOf(false)
    val isDynamicThemingEnabled = mutableStateOf(false)
    val isThemingSetToDefault = mutableStateOf(true)

    val nasaAPIKey = mutableStateOf("")
    val newsApiAPIKey = mutableStateOf("")
}