package com.sakethh.jetspacer

import androidx.compose.runtime.mutableStateOf

object CurrentHTTPCodes {
    val apodCurrentHTTPCode = mutableStateOf(200)
    val apodPaginationHTTPCode = mutableStateOf(200)
    val apodParticularDateDataHTTPCode = mutableStateOf(200)
    val ipGeoLocationCurrentHttpCode = mutableStateOf(200)
    val newsAPICurrentHttpCode = mutableStateOf(200)
    val marsRoversDataHTTPCode = mutableStateOf(200)
}