package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.sakethh.jetspacer.screens.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import com.sakethh.jetspacer.ui.theme.AppTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen() {
    val homeScreenViewModel:HomeScreenViewModel = viewModel()
    val systemUIController = rememberSystemUiController()
    systemUIController.setStatusBarColor(MaterialTheme.colorScheme.surface)
    val issInfo = "The \"International Space Station\" is currently over ${
        homeScreenViewModel.issLocationFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.latitude
    }° N, ${
        homeScreenViewModel.issLocationFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.longitude
    }° E"
    val issLatitude = homeScreenViewModel.issLocationFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.latitude

    val issLongitude = homeScreenViewModel.issLocationFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.longitude

    val issTimestamp = homeScreenViewModel.issLocationFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.timestamp.toString()


    val currentTimeInfo = "Current Time: ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.current_time
    }\nDate : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.date
    }\nDay Length : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.day_length
    }"
    // moon info
    val moonAltitude = homeScreenViewModel.moonAltitude.collectAsState(IPGeoLocationDTO()).value
    val moonAzimuthValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.moon_azimuth.toString()

    val moonDistanceValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.moon_distance.toString()
    val moonParalyticAngleValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue =  IPGeoLocationDTO()
        ).value.moon_parallactic_angle.toString()
    val moonRiseValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue = IPGeoLocationDTO()
    ).value.moonrise.toString()
    val moonSetValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue  = IPGeoLocationDTO()
    ).value.moonset.toString()
// sun info
    val solarNoonValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
            initialValue  = IPGeoLocationDTO()
        ).value.solar_noon.toString()
    val sunAltitudeValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.sun_altitude.toString()

    val sunAzimuthValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.sun_azimuth.toString()

    val sunDistanceValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.sun_distance.toString()
    val sunRiseValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue = IPGeoLocationDTO()
    ).value.sunrise.toString()
    val sunSetValue = homeScreenViewModel.astronomicalDataFromAPIFlow.value.collectAsStateWithLifecycle(
        initialValue =  IPGeoLocationDTO()
    ).value.sunset.toString()

    val apodURL =  homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val apodTitle = homeScreenViewModel.apodDataFromAPI.value.title.toString()
    val apodDescription = homeScreenViewModel.apodDataFromAPI.value.explanation.toString()
    val apodDate =  homeScreenViewModel.apodDataFromAPI.value.date.toString()
    val constraintSet = ConstraintSet {

        val cardIconConstraintRef = createRefFor("cardIcon")
        val cardTitleConstraintRef = createRefFor("cardTitle")
        val titleWithIconConstraintRef = createRefFor("titleWithIcon")
        val cardDescriptionConstraintRef = createRefFor("cardDescription")

        val apodMediaConstraintRef = createRefFor("apodMedia")
        val apodTitleConstraintRef = createRefFor("apodTitle")
        val apodDescriptionConstraintRef = createRefFor("apodDescription")
        val apodIconConstraintRef = createRefFor("apodIcon")
        val apodDropDownIconConstraintRef = createRefFor("apodDropDownIcon")


        constrain(cardIconConstraintRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(cardTitleConstraintRef) {
            top.linkTo(cardIconConstraintRef.top)
            start.linkTo(cardIconConstraintRef.end)
            bottom.linkTo(cardDescriptionConstraintRef.top)
        }
        constrain(cardDescriptionConstraintRef) {
            top.linkTo(cardTitleConstraintRef.bottom)
            start.linkTo(cardIconConstraintRef.end)
            bottom.linkTo(cardIconConstraintRef.bottom)
        }
        constrain(titleWithIconConstraintRef) {
            top.linkTo(cardIconConstraintRef.top)
            start.linkTo(cardIconConstraintRef.end)
            bottom.linkTo(cardIconConstraintRef.bottom)
        }


        constrain(apodMediaConstraintRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(apodIconConstraintRef) {
            top.linkTo(apodMediaConstraintRef.bottom)
            start.linkTo(parent.start)
        }
        constrain(apodTitleConstraintRef) {
            top.linkTo(apodIconConstraintRef.top)
            start.linkTo(apodIconConstraintRef.end)
            bottom.linkTo(apodDescriptionConstraintRef.top)
        }
        constrain(apodDescriptionConstraintRef) {
            top.linkTo(apodTitleConstraintRef.bottom)
            start.linkTo(apodIconConstraintRef.end)
            bottom.linkTo(apodIconConstraintRef.bottom)
        }
        constrain(apodDropDownIconConstraintRef) {
            top.linkTo(apodTitleConstraintRef.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Text(
                text = homeScreenViewModel.currentPhaseOfDay,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 15.dp, top = 30.dp)
            )
        }
        /*Geolocation*/
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    ConstraintLayout(constraintSet = constraintSet) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Icon Of \"My Location\"",
                            modifier = Modifier
                                .padding(top = 15.dp, start = 15.dp)
                                .size(25.dp)
                                .layoutId("cardIcon"),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Your Geolocation Data",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp)
                                .layoutId("cardTitle")
                        )
                        Text(
                            text = "Based on your I.P address",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 2.dp)
                                .layoutId("cardDescription")
                        )
                    }
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    )
                    Text(
                        text = "I.P address : ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.ip}\nLocation : ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.district}, ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.city}, ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.state_prov}, ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.country_name}.",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 0.dp,
                            bottom = 15.dp
                        )
                    )
                    Text(
                        text = "Zip code : ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.zipcode}\n" +
                                "Latitude and Longitude of the city : ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.latitude}, ${homeScreenViewModel.geolocationDTODataFromAPI.value.location?.longitude}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        /*ISS Location*/
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    ConstraintLayout(constraintSet = constraintSet) {
                        Icon(
                            painter = painterResource(id = R.drawable.satellite_icon),
                            contentDescription = "Icon Of \"Satellite\"",
                            modifier = Modifier
                                .padding(top = 15.dp, start = 15.dp)
                                .size(25.dp)
                                .layoutId("cardIcon"),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "ISS Info",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp)
                                .layoutId("titleWithIcon")
                        )
                    }
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    )
                    Text(
                        text = issInfo,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    )
                    CardRowGrid(
                        lhsCardTitle = "Latitude",
                        lhsCardValue = issLatitude,
                        rhsCardTitle = "Longitude",
                        rhsCardValue = issLongitude
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CardRowGrid(
                        lhsCardTitle = "Time Stamp",
                        lhsCardValue = issTimestamp,
                        rhsCardTitle = "",
                        rhsCardValue = "",
                        rhsCardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
        /*APOD*/
        item {

            val isIconDownwards = rememberSaveable {
                mutableStateOf(true)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.animateContentSize()) {
                    ConstraintLayout(constraintSet = constraintSet) {
                        when (homeScreenViewModel.apodDataFromAPI.value.media_type) {
                            "image" -> {
                                Coil_Image().CoilImage(
                                    imgURL = apodURL,
                                    contentDescription = "Today's \"Astronomy Picture Of The Day\" image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .layoutId("apodMedia"),
                                    onError = painterResource(id = R.drawable.ic_launcher_background),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            "video" -> {
                                WebViewModified(
                                    url = apodURL, modifier = Modifier
                                        .fillMaxWidth()
                                        .height(450.dp)
                                        .layoutId("apodMedia")
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Icon Of \"Image\"",
                            modifier = Modifier
                                .padding(top = 15.dp, start = 15.dp)
                                .size(25.dp)
                                .layoutId("apodIcon"),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "APOD",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 30.dp)
                                .layoutId("apodTitle")
                        )
                        Text(
                            text = "Astronomy Picture Of The Day\non $apodDate",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 2.dp)
                                .layoutId("apodDescription"),
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Start
                        )
                        val currentDropDownIcon =
                            if (isIconDownwards.value) {
                                Icons.Default.ArrowDropDown
                            } else {
                                Icons.Default.ArrowDropUp
                            }

                        Icon(
                            imageVector = currentDropDownIcon,
                            contentDescription = "dropDown",
                            modifier = Modifier
                                .padding(top = 10.dp, end = 15.dp)
                                .size(32.dp)
                                .layoutId("apodDropDownIcon")
                                .clickable {
                                    isIconDownwards.value = !isIconDownwards.value
                                },
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    )
                    Text(
                        text = "Title : $apodTitle",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    if (!isIconDownwards.value) {
                        Text(
                            text = apodDescription,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

        }
        /*Astronomical Data*/
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    ConstraintLayout(constraintSet = constraintSet) {
                        Icon(
                            painter = painterResource(id = R.drawable.list_icon),
                            contentDescription = "Icon Of \"DataExploration\"",
                            modifier = Modifier
                                .padding(top = 15.dp, start = 15.dp)
                                .size(25.dp)
                                .layoutId("cardIcon"),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Astronomical Data",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp)
                                .layoutId("cardTitle")
                        )
                        Text(
                            text = "Based on your I.P address",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 2.dp)
                                .layoutId("cardDescription")
                        )
                    }
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    )
                    Text(
                        text = currentTimeInfo,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 0.dp,
                            bottom = 15.dp
                        )
                    )
                    // moon info
                    CardRowGrid(
                        lhsCardTitle = "Moon Altitude",
                        lhsCardValue = moonAltitude.moon_altitude.toString(),
                        rhsCardTitle = "Moon Azimuth",
                        rhsCardValue = moonAzimuthValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CardRowGrid(
                        lhsCardTitle = "Moon Distance",
                        lhsCardValue = moonDistanceValue,
                        rhsCardTitle = "Moon Paralytic Angle",
                        rhsCardValue = moonParalyticAngleValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CardRowGrid(
                        lhsCardTitle = "Moon Rise",
                        lhsCardValue = moonRiseValue,
                        rhsCardTitle = "Moon Set",
                        rhsCardValue = moonSetValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Divider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    // sun info
                    CardRowGrid(
                        lhsCardTitle = "Sun Altitude",
                        lhsCardValue = sunAltitudeValue,
                        rhsCardTitle = "Sun Azimuth",
                        rhsCardValue = sunAzimuthValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CardRowGrid(
                        lhsCardTitle = "Sun Distance",
                        lhsCardValue = sunDistanceValue,
                        rhsCardTitle = "Sun Rise",
                        rhsCardValue = sunRiseValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CardRowGrid(
                        lhsCardTitle = "Sun Set",
                        lhsCardValue = sunSetValue,
                        rhsCardTitle = "Solar Noon",
                        rhsCardValue = solarNoonValue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}


@Composable
fun CardRowGrid(
    lhsCardTitle: String,
    lhsCardValue: String,
    rhsCardTitle: String,
    rhsCardValue: String,
    lhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    rhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
) {
    AppTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CardForRowGridRaw(
                title = lhsCardTitle,
                value = lhsCardValue,
                cardColors = lhsCardColors
            )
            CardForRowGridRaw(
                title = rhsCardTitle,
                value = rhsCardValue,
                cardColors = rhsCardColors
            )
        }
    }
}

@Composable
fun CardForRowGridRaw(
    title: String,
    value: String,
    cardModifier: Modifier = Modifier
        .height(85.dp)
        .width(150.dp),
    cardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
) {
    AppTheme {
        Card(
            modifier = cardModifier,
            colors = cardColors
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    lineHeight = 16.sp,
                    softWrap = true,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    lineHeight = 18.sp,
                    softWrap = true,
                    textAlign = TextAlign.Start, modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewModified(url: String, modifier: Modifier) {
    val webViewState = rememberWebViewState(url = url)
    WebView(state = webViewState, modifier = modifier, onCreated = { webView ->
        webView.settings.javaScriptEnabled = true
    })
}