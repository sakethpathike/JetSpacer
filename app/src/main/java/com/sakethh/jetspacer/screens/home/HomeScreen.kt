package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SelectableChipColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.sakethh.jetspacer.screens.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabled
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val systemUIController = rememberSystemUiController()
    systemUIController.setStatusBarColor(MaterialTheme.colorScheme.surface)
    systemUIController.setNavigationBarColor(MaterialTheme.colorScheme.primaryContainer)
    val activity = LocalContext.current as? Activity

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            activity?.finish()
        }
    }
    val issInfo = "The \"International Space Station\" is currently over ${
        homeScreenViewModel.issLocationFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.latitude
    }° N, ${
        homeScreenViewModel.issLocationFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.longitude
    }° E"
    val issLatitude = homeScreenViewModel.issLocationFromAPIFlow.collectAsStateWithLifecycle(
        initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.latitude

    val issLongitude = homeScreenViewModel.issLocationFromAPIFlow.collectAsStateWithLifecycle(
        initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.longitude

    val issTimestamp = homeScreenViewModel.issLocationFromAPIFlow.collectAsStateWithLifecycle(
        initialValue = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.timestamp.toString()


    val currentTimeInfo = "Current Time: ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.current_time
    }\nDate : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.date
    }\nDay Length : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.day_length
    }"


    // moon info
    val moonAltitude =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moon_altitude.toString()
    val moonAzimuthValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moon_azimuth.toString()

    val moonDistanceValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moon_distance.toString()
    val moonParalyticAngleValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moon_parallactic_angle.toString()
    val moonRiseValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moonrise.toString()
    val moonSetValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.moonset.toString()


// sun info
    val solarNoonValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.solar_noon.toString()
    val sunAltitudeValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.sun_altitude.toString()

    val sunAzimuthValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.sun_azimuth.toString()

    val sunDistanceValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.sun_distance.toString()
    val sunRiseValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.sunrise.toString()
    val sunSetValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsStateWithLifecycle(
            initialValue = IPGeoLocationDTO()
        ).value.sunset.toString()


    val apodURL = homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val apodTitle = homeScreenViewModel.apodDataFromAPI.value.title.toString()
    val apodDescription = homeScreenViewModel.apodDataFromAPI.value.explanation.toString()
    val apodDate = homeScreenViewModel.apodDataFromAPI.value.date.toString()
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

        val moreOptionsIcon = createRefFor("moreOptionsIcon")
        val moreOptionsDropDown = createRefFor("moreOptionsDropDown")
        val bookMarkIcon = createRefFor("bookMarkIcon")
        val downloadIcon = createRefFor("downloadIcon")


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


        constrain(moreOptionsIcon) {
            top.linkTo(apodMediaConstraintRef.top)
            end.linkTo(apodMediaConstraintRef.end)
        }
        constrain(moreOptionsDropDown) {
            top.linkTo(apodMediaConstraintRef.top)
            end.linkTo(apodMediaConstraintRef.end)
        }
        constrain(downloadIcon) {
            bottom.linkTo(bookMarkIcon.top)
            end.linkTo(apodMediaConstraintRef.end)
        }
        constrain(bookMarkIcon) {
            bottom.linkTo(apodMediaConstraintRef.bottom)
            end.linkTo(apodMediaConstraintRef.end)
        }

    }
    ModalBottomSheetLayout(
        sheetContent = {
            APODBottomSheetContent(
                homeScreenViewModel = homeScreenViewModel,
                apodURL = apodURL,
                apodTitle = apodTitle,
                apodDate = apodDate,
                apodDescription = apodDescription
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = homeScreenViewModel.currentPhaseOfDay,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(start = 15.dp, top = 30.dp)
                    )
                    APODSideIconButton(
                        imageVector = Icons.Outlined.Settings,
                        onClick = { },
                        iconBtnModifier = Modifier
                            .padding(end = 15.dp, top = 20.dp)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ),
                        iconBtnColor = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        iconModifier = Modifier
                    )
                }
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
                                .padding(start = 15.dp, end = 15.dp),
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
                APODCardComposable(
                    homeScreenViewModel = homeScreenViewModel,
                    apodURL = apodURL,
                    apodDate = apodDate,
                    apodDescription = apodDescription,
                    apodTitle = apodTitle,
                    imageOnClick = {
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }
                )
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
                            lhsCardValue = moonAltitude,
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
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}


@Composable
fun CardRowGrid(
    lhsCardTitle: String,
    lhsCardValue: String,
    lhsOnClick: () -> Unit = {},
    rhsCardTitle: String,
    rhsCardValue: String,
    lhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    rhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    rhsOnClick: () -> Unit = {}
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
                cardColors = lhsCardColors,
                cardModifier = Modifier
                    .height(85.dp)
                    .width(150.dp)
                    .clickable {
                        lhsOnClick()
                    }
            )
            CardForRowGridRaw(
                title = rhsCardTitle,
                value = rhsCardValue,
                cardColors = rhsCardColors,
                cardModifier = Modifier
                    .height(85.dp)
                    .width(150.dp)
                    .clickable {
                        rhsOnClick()
                    }
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
    cardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    inSpaceScreen: Boolean = false,
    imageHeight: Dp = 110.dp,
    imgURL: String = ""
) {
    val lhsTextColumnModifier = if (!inSpaceScreen) {
        Modifier.padding(15.dp)
    } else {
        Modifier
            .padding(15.dp)
            .fillMaxWidth(0.55f)
    }
    AppTheme {
        Card(
            modifier = cardModifier,
            colors = cardColors
        ) {
            Row {
                Column(
                    modifier = lhsTextColumnModifier
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
                if (inSpaceScreen) {
                    Box(
                        Modifier.background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(
                                        0.3f
                                    ), MaterialTheme.colorScheme.primaryContainer.copy(1f)
                                )
                            )
                        )
                    ) {
                        Coil_Image().CoilImage(
                            imgURL = imgURL,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight),
                            onError = painterResource(id = R.drawable.satellite_filled),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewModified(url: String?, embedString: String? = null, modifier: Modifier) {
    val webViewState = url?.let { rememberWebViewState(url = it) }
    if (webViewState != null) {
        WebView(state = webViewState, modifier = modifier, onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
            if (embedString != null) {
                webView.loadData(embedString, "text/html", null)
            }
        })
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)
@Composable
fun APODCardComposable(
    homeScreenViewModel: HomeScreenViewModel,
    apodURL: String,
    apodDate: String,
    apodDescription: String,
    apodTitle: String,
    cardTopPaddingValue:Dp=30.dp,
    inBookMarkScreen: Boolean = false,
    inSpaceScreen: Boolean = false,
    imageOnClick: () -> Unit = {},
    changeDateChipOnClick:()->Unit={}
) {
    val context = LocalContext.current
    val isIconDownwards = rememberSaveable {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = cardTopPaddingValue)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            ConstraintLayout(constraintSet = constraintSet) {
                APODImageLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    apodURL = apodURL,
                    apodTitle = apodTitle,
                    apodDate = apodDate,
                    apodDescription = apodDescription,
                    imageOnClick = {
                        imageOnClick()
                    }
                )
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
                if (inSpaceScreen) {
                    AssistChip(
                        modifier = Modifier.layoutId("changeAPODDate"),
                        onClick = { changeDateChipOnClick()},
                        label = {
                            Text(
                                text = "Change APOD Date",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }, trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ImageSearch,
                                modifier = Modifier.size(InputChipDefaults.IconSize),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        shape = RoundedCornerShape(5.dp),
                        colors = AssistChipDefaults.assistChipColors(),
                        border = AssistChipDefaults.assistChipBorder(borderWidth = 0.dp, borderColor = MaterialTheme.colorScheme.onPrimary)
                    )
                }
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

            if (isAlertDialogEnabled.value) {
                AlertDialog(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onDismissRequest = { isAlertDialogEnabled.value = false },
                    title = {
                        Text(
                            text = "Wait a minute!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 18.sp
                        )
                    }, text = {
                        Text(
                            text = "Are you sure want to remove this APOD publication from bookmarks which is stored locally on your device? This can't be undone.",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSecondary,
                            lineHeight = 18.sp
                        )
                    }, confirmButton = {
                        Button(
                            onClick = {
                                isAlertDialogEnabled.value = false
                                coroutineScope.launch {
                                    homeScreenViewModel.dbImplementation.deleteFromAPODDB(
                                        apodURL
                                    )
                                    homeScreenViewModel.doesThisExistsInAPODIconTxt(
                                        apodURL
                                    )
                                    if (!homeScreenViewModel.dbUtils.doesThisExistsInDBAPOD(
                                            apodURL
                                        )
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Removed the APOD publication from bookmarks",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Something went wrong:(",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Text(
                                text = "Remove it ASAP!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }, dismissButton = {
                        OutlinedButton(
                            onClick = { isAlertDialogEnabled.value = false },
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                text = "Um-mm, Never mind",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    })
            }
            if (inBookMarkScreen) {
                AssistChip(
                    border = null,
                    modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                    onClick = { },
                    label = {
                        Text(
                            text = "APOD",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 12.sp
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }

    coroutineScope.launch {
        homeScreenViewModel.doesThisExistsInAPODIconTxt(apodURL)
    }
}

@Composable
fun APODSideIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    iconBtnColor: androidx.compose.ui.graphics.Color,
    @SuppressLint("ModifierParameter") iconBtnModifier: Modifier = Modifier.background(
        shape = CircleShape,
        color = iconBtnColor
    ),
    iconColor: androidx.compose.ui.graphics.Color,
    iconModifier: Modifier
) {
    IconButton(
        onClick = { onClick() },
        modifier = iconBtnModifier,
        interactionSource = MutableInteractionSource()
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = iconColor,
            modifier = iconModifier
        )
    }
}

@Composable
fun DropDownMenuItemModified(text: String, onClick: () -> Unit, imageVector: ImageVector) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        onClick = { onClick() },
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    )
}

@Composable
fun APODImageLayout(
    homeScreenViewModel: HomeScreenViewModel,
    apodURL: String,
    apodTitle: String,
    apodDate: String,
    apodDescription: String,
    inAPODScreen: Boolean = false,
    imageOnClick: () -> Unit = {}
) {

    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()
    val apodIconButtonTransparencyValue = 0.4f
    val apodIconTransparencyValue = 0.9f
    val isMoreClicked = rememberSaveable { mutableStateOf(false) }
    when (homeScreenViewModel.apodDataFromAPI.value.media_type) {
        "image" -> {
            Coil_Image().CoilImage(
                imgURL = apodURL,
                contentDescription = "Today's \"Astronomy Picture Of The Day\" image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        imageOnClick()
                    }
                    .layoutId("apodMedia"),
                onError = painterResource(id = R.drawable.ic_launcher_background),
                contentScale = ContentScale.Crop
            )
            BoxWithConstraints(
                modifier = Modifier
                    .layoutId("moreOptionsIcon")
            ) {
                APODSideIconButton(
                    imageVector = Icons.Default.MoreVert,
                    onClick = {
                        isMoreClicked.value = true
                    },
                    iconBtnColor = MaterialTheme.colorScheme.secondary.copy(
                        apodIconButtonTransparencyValue
                    ),
                    iconBtnModifier = Modifier
                        .padding(top = 10.dp, end = 10.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary.copy(
                                apodIconButtonTransparencyValue
                            )
                        )
                        .layoutId("moreOptionsIcon"),
                    iconColor = MaterialTheme.colorScheme.onSecondary.copy(
                        apodIconTransparencyValue
                    ),
                    iconModifier = Modifier
                )
                DropdownMenu(
                    expanded = isMoreClicked.value,
                    onDismissRequest = { isMoreClicked.value = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .layoutId("moreOptionsDropDown")
                ) {
                    DropDownMenuItemModified(
                        text = "Open in browser",
                        onClick = {
                            isMoreClicked.value = false
                            localUriHandler.openUri("https://apod.nasa.gov/apod/")
                        },
                        imageVector = Icons.Outlined.OpenInBrowser
                    )
                    DropDownMenuItemModified(
                        text = "Copy image link",
                        onClick = {
                            isMoreClicked.value = false
                            localClipboardManager.setText(AnnotatedString(apodURL))
                            Toast.makeText(
                                context,
                                "Image URL copied to clipboard",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        imageVector = Icons.Outlined.ContentCopy
                    )
                    DropDownMenuItemModified(
                        text = "Share",
                        onClick = {
                            isMoreClicked.value = false
                            val intent = Intent()
                            intent.action = Intent.ACTION_SEND
                            intent.type = "text/plain"
                            intent.putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out today's APOD:\nhttps://apod.nasa.gov/apod/"
                            )
                            val shareIntent =
                                Intent.createChooser(intent, "Share using :-")
                            context.startActivity(shareIntent)
                        },
                        imageVector = Icons.Outlined.Share
                    )
                    DropDownMenuItemModified(
                        text = homeScreenViewModel.bookMarkText.value,
                        onClick = {
                            homeScreenViewModel.doesThisExistsInAPODIconTxt(apodURL)
                            coroutineScope.launch {
                                homeScreenViewModel.dbImplementation.addNewBookMarkToAPODDB(
                                    APOD_DB_DTO().apply {
                                        id = apodURL
                                        title = apodTitle
                                        datePublished = apodDate
                                        description = apodDescription
                                        imageURL = apodURL
                                        isBookMarked = true
                                    }
                                )
                            }
                            if (!homeScreenViewModel.dbUtils.doesThisExistsInDBAPOD(
                                    apodURL
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    "Added to bookmarks:)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            isMoreClicked.value = false
                        },
                        imageVector = homeScreenViewModel.bookMarkIcons.value
                    )
                    DropDownMenuItemModified(
                        text = "Download",
                        onClick = { isMoreClicked.value = false },
                        imageVector = Icons.Outlined.FileDownload
                    )
                }
            }

            if (!inAPODScreen) {
                APODSideIconButton(
                    imageVector = Icons.Outlined.FileDownload,
                    onClick = { },
                    iconBtnColor = MaterialTheme.colorScheme.secondary.copy(
                        apodIconButtonTransparencyValue
                    ),
                    iconBtnModifier = Modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary.copy(
                                apodIconButtonTransparencyValue
                            )
                        )
                        .layoutId("downloadIcon"),
                    iconColor = MaterialTheme.colorScheme.onSecondary.copy(
                        apodIconTransparencyValue
                    ),
                    iconModifier = Modifier
                )
                APODSideIconButton(
                    imageVector = homeScreenViewModel.bookMarkIcons.value,
                    onClick = {
                        coroutineScope.launch {
                            homeScreenViewModel.dbImplementation.addNewBookMarkToAPODDB(
                                APOD_DB_DTO().apply {
                                    id = apodURL
                                    title = apodTitle
                                    datePublished = apodDate
                                    description = apodDescription
                                    imageURL = apodURL
                                    isBookMarked = true
                                }
                            )
                            homeScreenViewModel.doesThisExistsInAPODIconTxt(apodURL)
                        }
                        if (!homeScreenViewModel.dbUtils.doesThisExistsInDBAPOD(apodURL)) {
                            Toast.makeText(
                                context,
                                "Added to bookmarks:)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    iconBtnColor = MaterialTheme.colorScheme.secondary.copy(
                        apodIconButtonTransparencyValue
                    ),
                    iconBtnModifier = Modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary.copy(
                                apodIconButtonTransparencyValue
                            )
                        )
                        .layoutId("bookMarkIcon"),
                    iconColor = MaterialTheme.colorScheme.onSecondary.copy(
                        apodIconTransparencyValue
                    ),
                    iconModifier = Modifier
                )
            }
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

}