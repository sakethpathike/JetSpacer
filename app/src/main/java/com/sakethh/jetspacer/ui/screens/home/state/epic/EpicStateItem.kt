package com.sakethh.jetspacer.ui.screens.home.state.epic


data class EpicStateItem(
    val imageURL: String,
    val date: String,
    val timeWhenImageWasCaptured: String,
    val distanceToEarthFromTheEPIC: Long,
    val distanceBetweenSunAndEarth: Long,
    val distanceBetweenEarthAndMoon: Long,
    val distanceBetweenSunAndEpic: Long
)
