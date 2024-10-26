package com.sakethh.jetspacer.home.presentation.state.epic


data class EpicStateItem(
    val imageURL: String,
    val date: String,
    val timeWhenImageWasCaptured: String,
    val distanceToEarthFromTheEPIC: Long,
    val distanceToSunFromEPIC: Long,
    val distanceBetweenEarthToMoon: Long,
    val distanceBetweenSunAndEarth: Long = distanceToSunFromEPIC + distanceToEarthFromTheEPIC
)
