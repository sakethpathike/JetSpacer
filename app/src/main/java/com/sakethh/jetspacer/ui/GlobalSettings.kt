package com.sakethh.jetspacer.ui

import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.common.utils.Constants

object GlobalSettings {
    val isDarkModeEnabled = mutableStateOf(false)
    val isDynamicThemingEnabled = mutableStateOf(false)
    val isThemingSetToDefault = mutableStateOf(true)

    val nasaAPIKey = mutableStateOf(Constants.NASA_API_KEY)
    val newsApiAPIKey = mutableStateOf(Constants.NEWS_API_KEY)
}