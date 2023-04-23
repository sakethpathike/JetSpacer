@file:OptIn(ExperimentalFoundationApi::class)

package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.downloads.DownloadImpl
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.BtmSaveComposableContent
import com.sakethh.jetspacer.screens.constraintSet
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import io.ktor.util.reflect.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val activity = LocalContext.current as? Activity
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
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
        homeScreenViewModel.issLocationFromAPIFlow.collectAsState(
            initial = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.latitude
    }° N, ${
        homeScreenViewModel.issLocationFromAPIFlow.collectAsState(
            initial = ISSLocationDTO(IssPosition("", ""), "", 0)
        ).value.iss_position.longitude
    }° E"
    val issLatitude = homeScreenViewModel.issLocationFromAPIFlow.collectAsState(
        initial = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.latitude

    val issLongitude = homeScreenViewModel.issLocationFromAPIFlow.collectAsState(
        initial = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.iss_position.longitude

    val issTimestamp = homeScreenViewModel.issLocationFromAPIFlow.collectAsState(
        initial = ISSLocationDTO(IssPosition("", ""), "", 0)
    ).value.timestamp.toString()


    val currentTimeInfo = "Current Time: ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.current_time
    }\nDate : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.date
    }\nDay Length : ${
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.day_length
    }"


    // moon info
    val moonAltitude =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moon_altitude.toString()
    val moonAzimuthValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moon_azimuth.toString()

    val moonDistanceValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moon_distance.toString()
    val moonParalyticAngleValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moon_parallactic_angle.toString()
    val moonRiseValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moonrise.toString()
    val moonSetValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.moonset.toString()


// sun info
    val solarNoonValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.solar_noon.toString()
    val sunAltitudeValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.sun_altitude.toString()

    val sunAzimuthValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.sun_azimuth.toString()

    val sunDistanceValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.sun_distance.toString()
    val sunRiseValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.sunrise.toString()
    val sunSetValue =
        homeScreenViewModel.astronomicalDataFromAPIFlow.collectAsState(
            initial = IPGeoLocationDTO()
        ).value.sunset.toString()

    val context = LocalContext.current
    val apodURL = homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val apodTitle = homeScreenViewModel.apodDataFromAPI.value.title.toString()
    val apodDescription = homeScreenViewModel.apodDataFromAPI.value.explanation.toString()
    val apodDate = homeScreenViewModel.apodDataFromAPI.value.date.toString()
    val apodMediaType = homeScreenViewModel.apodDataFromAPI.value.media_type.toString()
    val bookMarksVM: BookMarksVM = viewModel()
    var didDataGetAddedInDB = false
    val isRefreshing = remember { mutableStateOf(false) }
    val isConnectedToInternet =
        HomeScreenViewModel.Network.connectedToInternet.collectAsState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                coroutineScope.launch {
                    awaitAll(
                        async {
                            withContext(Dispatchers.Main) {
                                if (isConnectedToInternet.value) {
                                    Toast.makeText(
                                        context,
                                        "Refreshing data in a moment",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                delay(2000L)
                                isRefreshing.value = false
                            }
                        },
                        async {
                            try {
                                HomeScreenViewModel.Network.isConnectionSucceed.value = true
                                homeScreenViewModel.loadData()
                            } catch (_: Exception) {
                                HomeScreenViewModel.Network.isConnectionSucceed.value = false
                                withContext(Dispatchers.Main) {
                                    isRefreshing.value = false
                                    Toast.makeText(
                                        context,
                                        "Network not detected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
            }
        )
    bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)
    ModalBottomSheetLayout(
        sheetContent = {
            Box(modifier = Modifier.size(1.dp))
            when (homeScreenViewModel.currentBtmSheetType.value) {
                HomeScreenViewModel.BtmSheetType.Details -> {
                    bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)
                    APODBottomSheetContent(
                        homeScreenViewModel = homeScreenViewModel,
                        apodURL = apodURL,
                        apodTitle = apodTitle,
                        apodDate = apodDate,
                        apodDescription = apodDescription,
                        apodMediaType = apodMediaType,
                        onBookMarkClick = {
                            triggerHapticFeedback(context = context)
                            bookMarksVM.imgURL = apodURL
                            coroutineScope.launch {
                                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                                val formattedDate = dateFormat.format(Date())
                                didDataGetAddedInDB =
                                    bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                        this.title = apodTitle
                                        this.datePublished = apodDate
                                        this.description = apodDescription
                                        this.imageURL = apodURL
                                        this.mediaType = "image"
                                        this.isBookMarked = true
                                        this.category = "APOD"
                                        this.hdImageURL =
                                            homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                                        this.addedToLocalDBOn = formattedDate
                                    })
                            }.invokeOnCompletion {
                                if (didDataGetAddedInDB) {
                                    Toast.makeText(
                                        context,
                                        "Added to bookmarks:)",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                        true
                                }
                                bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                            }
                        },
                        imageHDURL = homeScreenViewModel.apodDataFromAPI.value.hdurl.toString(),
                        onBookMarkLongPress = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }.invokeOnCompletion {
                                homeScreenViewModel.currentBtmSheetType.value =
                                    HomeScreenViewModel.BtmSheetType.BookMarkCollection
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }
                            }
                        }
                    )
                }

                HomeScreenViewModel.BtmSheetType.BookMarkCollection -> {
                    BtmSaveComposableContent(
                        coroutineScope = coroutineScope,
                        modalBottomSheetState = bottomSheetState,
                        data = CustomBookMarkData(dataType = SavedDataType.APOD, data = APOD_DB_DTO().apply {
                            this.imageURL =
                                homeScreenViewModel.apodDataFromAPI.value.url.toString()
                            this.hdImageURL =
                                homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                            this.category =
                                homeScreenViewModel.apodDataFromAPI.value.media_type.toString()
                            this.isBookMarked = true
                            this.datePublished =
                                homeScreenViewModel.apodDataFromAPI.value.date.toString()
                            this.description =
                                homeScreenViewModel.apodDataFromAPI.value.explanation.toString()
                            this.title =
                                homeScreenViewModel.apodDataFromAPI.value.title.toString()
                            this.mediaType =
                                homeScreenViewModel.apodDataFromAPI.value.media_type.toString()
                        })
                    )
                }
            }
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {

        Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween
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
                            onClick = {
                                navController.navigate(NavigationRoutes.SETTINGS_SCREEN)
                            },
                            iconBtnModifier = Modifier
                                .padding(end = 15.dp, top = 20.dp)
                                .background(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                            iconBtnColor = MaterialTheme.colorScheme.onPrimary,
                            iconColor = MaterialTheme.colorScheme.onPrimary,
                            iconModifier = Modifier
                        )
                    }
                }

                /*APOD*/
                var doesExistsInDB = false
                item {
                    APODCardComposable(
                        homeScreenViewModel = homeScreenViewModel,
                        imageURL = apodURL,
                        apodDate = apodDate,
                        apodDescription = apodDescription,
                        apodTitle = apodTitle,
                        imageOnClick = {
                            homeScreenViewModel.currentBtmSheetType.value =
                                HomeScreenViewModel.BtmSheetType.Details
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        },
                        apodMediaType = apodMediaType,
                        inAPODBottomSheetContent = false,
                        bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                        onBookMarkButtonClick = {
                            triggerHapticFeedback(context = context)
                            bookMarksVM.imgURL = apodURL
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                            val formattedDate = dateFormat.format(Date())
                            coroutineScope.launch {
                                didDataGetAddedInDB =
                                    bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                        this.title = apodTitle
                                        this.datePublished = apodDate
                                        this.description = apodDescription
                                        this.imageURL = apodURL
                                        this.mediaType = "image"
                                        this.isBookMarked = true
                                        this.category = "APOD"
                                        this.hdImageURL =
                                            homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                                        this.addedToLocalDBOn = formattedDate
                                    })
                            }.invokeOnCompletion {
                                if (didDataGetAddedInDB) {
                                    Toast.makeText(
                                        context,
                                        "Added to bookmarks:)",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                        true
                                }
                                bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                            }
                        },
                        onBookmarkLongPress = {
                            coroutineScope.launch {
                                if (bottomSheetState.isVisible) {
                                    bottomSheetState.hide()
                                }
                            }.invokeOnCompletion {
                                homeScreenViewModel.currentBtmSheetType.value =
                                    HomeScreenViewModel.BtmSheetType.BookMarkCollection
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }
                            }
                        },
                        capturedOnSol = "",
                        capturedBy = "",
                        roverName = "",
                        onConfirmButtonClick = {
                            triggerHapticFeedback(context = context)
                            coroutineScope.launch {
                                doesExistsInDB =
                                    bookMarksVM.deleteDataFromAPODDB(imageURL = bookMarksVM.imgURL)
                            }.invokeOnCompletion {
                                if (doesExistsInDB) {
                                    Toast.makeText(
                                        context,
                                        "Bookmark didn't got removed as expected, report it:(",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Removed from bookmarks:)",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                            }
                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                false
                        },
                        apodHDImageURL = homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                    )
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
                            androidx.compose.material3.Divider(
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
                            androidx.compose.material3.Divider(
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
                                isLHSShimmerVisible = issLatitude.isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,

                                isRHSShimmerVisible = issLongitude.isEmpty(),
                                isLHSShimmering = issLatitude.isEmpty(),
                                isRHSShimmering = issLongitude.isEmpty(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,

                                rhsCardTitle = "Longitude",
                                rhsCardValue = issLongitude
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Time Stamp",
                                lhsCardValue = issTimestamp,

                                isLHSShimmering = issTimestamp.toInt() == 0,
                                isLHSShimmerVisible = issTimestamp.toInt() == 0,
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "",
                                rhsCardValue = "",
                                rhsCardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
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
                            androidx.compose.material3.Divider(
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
                            androidx.compose.material3.Divider(
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
                                rhsCardValue = moonAzimuthValue,
                                isLHSShimmerVisible = moonAltitude == "null",
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = moonAzimuthValue == "null",
                                isLHSShimmering = moonAltitude == "null",
                                isRHSShimmering = moonAzimuthValue == "null",
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Moon Distance",
                                lhsCardValue = moonDistanceValue,
                                rhsCardTitle = "Moon Paralytic Angle",
                                rhsCardValue = moonParalyticAngleValue,
                                isLHSShimmerVisible = moonDistanceValue == "null",
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = moonParalyticAngleValue == "null",
                                isLHSShimmering = moonDistanceValue == "null",
                                isRHSShimmering = moonParalyticAngleValue == "null",
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Moon Rise",
                                lhsCardValue = moonRiseValue,
                                rhsCardTitle = "Moon Set",
                                rhsCardValue = moonSetValue,
                                isLHSShimmerVisible = moonRiseValue.isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = moonSetValue.isEmpty(),
                                isLHSShimmering = moonRiseValue.isEmpty(),
                                isRHSShimmering = moonSetValue.isEmpty(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            androidx.compose.material3.Divider(
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
                                rhsCardValue = sunAzimuthValue,
                                isLHSShimmerVisible = sunAltitudeValue == "null",
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = sunAzimuthValue == "null",
                                isLHSShimmering = sunAltitudeValue == "null",
                                isRHSShimmering = sunAzimuthValue == "null",
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
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
                                rhsCardValue = solarNoonValue,
                                isLHSShimmerVisible = sunSetValue.isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = solarNoonValue.isEmpty(),
                                isLHSShimmering = sunSetValue.isEmpty(),
                                isRHSShimmering = solarNoonValue.isEmpty(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing.value,
                state = pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                scale = true
            )
        }
        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        didDataGetAddedInDB =
                            bookMarksVM.deleteDataFromAPODDB(imageURL = apodURL)
                    }.invokeOnCompletion {
                        if (didDataGetAddedInDB) {
                            Toast.makeText(
                                context,
                                "Bookmark didn't got removed as expected, report it:(",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Removed from bookmarks:)",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                        false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                        false
                }
            )
        }
    }

}

fun triggerHapticFeedback(context: Context) {
    val hapticFeedback = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        hapticFeedback.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        hapticFeedback.vibrate(50)
    }
}

@Composable
fun CardRowGrid(
    lhsCardTitle: String,
    lhsCardValue: String,
    lhsOnClick: () -> Unit = {},
    isLHSShimmerVisible: Boolean? = null,
    isLHSShimmering: Boolean = false,
    lhsShimmerColor: Color? = null,
    lhsShimmerHighlightColor: Color? = null,
    rhsCardTitle: String,
    rhsCardValue: String,
    lhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    rhsCardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    rhsOnClick: () -> Unit = {},
    isRHSShimmerVisible: Boolean? = null,
    isRHSShimmering: Boolean = false,
    rhsShimmerColor: Color? = null,
    rhsShimmerHighlightColor: Color? = null,
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
                    },
                visible = isLHSShimmerVisible,
                color = lhsShimmerColor,
                shimmerHighLightColor = lhsShimmerHighlightColor,
                isShimmering = isLHSShimmering
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
                    },
                visible = isRHSShimmerVisible,
                color = rhsShimmerColor,
                shimmerHighLightColor = rhsShimmerHighlightColor,
                isShimmering = isRHSShimmering
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
    cardColors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    inSpaceScreen: Boolean = false,
    imageHeight: Dp = 110.dp,
    imgURL: String = "",
    visible: Boolean? = null,
    color: Color? = null,
    shimmerHighLightColor: Color? = null,
    isShimmering: Boolean = false,
    singleRawCard: Boolean? = null,
) {
    val lhsTextColumnModifier = if (!inSpaceScreen) {
        Modifier.padding(15.dp)
    } else {
        Modifier
            .padding(15.dp)
            .fillMaxWidth(0.55f)
    }
    val textModifier =
        if (visible != null && color != null && shimmerHighLightColor != null && isShimmering && singleRawCard == null) {
            Modifier
                .padding(top = 10.dp)
                .height(40.dp)
                .width(115.dp)
                .shimmer(
                    visible = visible,
                    color = color,
                    shimmerHighLightColor = shimmerHighLightColor
                )
        } else if (singleRawCard != null && visible != null && color != null && shimmerHighLightColor != null && isShimmering) {
            Modifier
                .padding(top = 10.dp)
                .height(20.dp)
                .fillMaxWidth(0.95f)
                .shimmer(
                    visible = visible,
                    color = color,
                    shimmerHighLightColor = shimmerHighLightColor
                )
        } else {
            Modifier.padding(top = 4.dp)
        }
    val height= remember {  mutableStateOf(0.dp) }
    val density= LocalDensity.current
    AppTheme {
        Card(
            modifier = cardModifier,
            colors = cardColors
        ) {
            Row {
                Column(
                    modifier = Modifier.onGloballyPositioned { 
                        height.value= with(density){
                            it.size.height.toDp()
                        }
                    }
                ) {
                    Column(
                        modifier = lhsTextColumnModifier
                    ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        lineHeight = 16.sp,
                        softWrap = true,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = value,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        lineHeight = 18.sp,
                        softWrap = true,
                        textAlign = TextAlign.Start, modifier = textModifier
                    )
                }
                }
                if (inSpaceScreen) {
                        Coil_Image().CoilImage(
                            imgURL = imgURL,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height.value),
                            onError = painterResource(id = R.drawable.satellite_filled),
                            contentScale = ContentScale.Crop
                        )
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
    imageURL: String = "",
    apodHDImageURL: String,
    apodDate: String = "",
    apodDescription: String = "",
    apodTitle: String = "",
    apodMediaType: String = "",
    cardTopPaddingValue: Dp = 30.dp,
    inBookMarkScreen: Boolean = false,
    inSpaceScreen: Boolean = false,
    imageOnClick: () -> Unit = {},
    bookMarkedCategory: String = "",
    changeDateChipOnClick: () -> Unit = {},
    inAPODBottomSheetContent: Boolean,
    onBookMarkButtonClick: () -> Unit,
    capturedOnSol: String,
    capturedBy: String,
    roverName: String,
    onConfirmButtonClick: () -> Unit = {},
    onBookmarkLongPress: () -> Unit = {},
) {
    val context = LocalContext.current

    val isIconDownwards = rememberSaveable {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()
    val bookMarksVM: BookMarksVM = viewModel()

    if (bookMarkedCategory == Constants.SAVED_IN_ROVERS_DB) {
        bookMarksVM.doesThisExistsInRoverDBIconTxt(
            imageURL
        )
    } else {
        bookMarksVM.doesThisExistsInAPODIconTxt(
            imageURL
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = cardTopPaddingValue)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            ConstraintLayout(constraintSet = constraintSet) {
                APODMediaLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    imageURL = imageURL,
                    imageOnClick = {
                        imageOnClick()
                    },
                    apodMediaType = apodMediaType,
                    inAPODBottomSheetContent = inAPODBottomSheetContent,
                    onBookMarkButtonClick = {
                        onBookMarkButtonClick()
                    },
                    hdImageURLForAPOD = apodHDImageURL,
                    onBookmarkLongPress = {
                        onBookmarkLongPress()
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
                val titleAfterImage: String =
                    if (bookMarkedCategory != Constants.SAVED_IN_ROVERS_DB) {
                        "APOD"
                    } else {
                        "Mars Rover"
                    }
                Text(
                    text = titleAfterImage,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 30.dp)
                        .layoutId("apodTitle")
                )
                val descriptionForTitle: String =
                    if (bookMarkedCategory != Constants.SAVED_IN_ROVERS_DB) {
                        "Astronomy Picture Of The Day\non $apodDate"
                    } else {
                        "Sol : $capturedOnSol"
                    }
                Text(
                    text = descriptionForTitle,
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
                        onClick = { changeDateChipOnClick() },
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
                        border = AssistChipDefaults.assistChipBorder(
                            borderWidth = 0.dp,
                            borderColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                val currentDropDownIcon =
                    if (isIconDownwards.value) {
                        Icons.Default.ArrowDropDown
                    } else {
                        Icons.Default.ArrowDropUp
                    }
                if (bookMarkedCategory != Constants.SAVED_IN_ROVERS_DB) {
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
            }
            androidx.compose.material3.Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
            )
            if (bookMarkedCategory != Constants.SAVED_IN_ROVERS_DB) {
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
            } else {
                CardForRowGridRaw(
                    title = "Captured by",
                    value = capturedBy,
                    cardModifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }

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
            if (isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
                AlertDialogForDeletingFromDB(
                    bookMarkedCategory = bookMarkedCategory,
                    onConfirmBtnClick = onConfirmButtonClick
                )
            }
            val categoryChipText: String =
                if (bookMarkedCategory == Constants.SAVED_IN_APOD_DB) {
                    "APOD"
                } else if (bookMarkedCategory == Constants.SAVED_IN_ROVERS_DB) {
                    roverName
                } else {
                    ""
                }
            if (inBookMarkScreen) {
                AssistChip(
                    border = null,
                    modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                    onClick = { },
                    label = {
                        Text(
                            text = categoryChipText,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 12.sp
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}

@Composable
fun AlertDialogForDeletingFromDB(
    bookMarkedCategory: String,
    onConfirmBtnClick: () -> Unit,
) {

    val dialogText: String = if (bookMarkedCategory == Constants.SAVED_IN_ROVERS_DB) {
        "Are you sure want to remove this image captured by the rover from bookmarks which is stored locally on your device? This can't be undone."
    } else if (bookMarkedCategory == Constants.SAVED_IN_NEWS_DB) {
        "Are you sure want to remove this NEWS publication from bookmarks which is stored locally on your device? This can't be undone."
    } else if (bookMarkedCategory == Constants.IN_SETTINGS_SCREEN) {
        "Are you sure want to remove all bookmarks which are stored locally on your device? This can't be undone."
    } else {
        "Are you sure want to remove this APOD publication from bookmarks which is stored locally on your device? This can't be undone."
    }
    AppTheme {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primary,
            onDismissRequest = {
                isAlertDialogEnabledForRoversDB.value =
                    false;isAlertDialogEnabledForAPODDB.value =
                false
            },
            title = {
                Text(
                    text = "Wait a minute!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                )
            }, text = {
                Text(
                    text = dialogText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 18.sp
                )
            }, confirmButton = {
                Button(
                    onClick = {
                        isAlertDialogEnabledForRoversDB.value = false
                        isAlertDialogEnabledForAPODDB.value = false
                        onConfirmBtnClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(
                        text = if (bookMarkedCategory == Constants.IN_SETTINGS_SCREEN) "Remove them ASAP!" else "Remove it ASAP!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }, dismissButton = {
                OutlinedButton(
                    onClick = {
                        isAlertDialogEnabledForRoversDB.value =
                            false;isAlertDialogEnabledForAPODDB.value = false
                    },
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Um-mm, Never mind",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            })

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
    iconModifier: Modifier,
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
fun DropDownMenuItemModified(
    text: String,
    onClick: () -> Unit,
    imageVector: ImageVector,
) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun APODMediaLayout(
    homeScreenViewModel: HomeScreenViewModel,
    imageURL: String,
    hdImageURLForAPOD: String,
    onBookMarkButtonClick: () -> Unit,
    inAPODBottomSheetContent: Boolean,
    imageOnClick: () -> Unit = {},
    apodMediaType: String,
    onBookmarkLongPress: () -> Unit = {},
) {
    val bookMarksVM: BookMarksVM = viewModel()
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()
    val apodIconButtonTransparencyValue = 0.4f
    val apodIconTransparencyValue = 0.9f
    val isMoreClicked = rememberSaveable { mutableStateOf(false) }

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    when (apodMediaType) {
        "image" -> {
            Coil_Image().CoilImage(
                imgURL = imageURL,
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
                            localUriHandler.openUri(imageURL)
                        },
                        imageVector = Icons.Outlined.OpenInBrowser
                    )
                    DropDownMenuItemModified(
                        text = "Copy image link",
                        onClick = {
                            isMoreClicked.value = false
                            localClipboardManager.setText(AnnotatedString(imageURL))
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
                        text = bookMarksVM.bookMarkText.value,
                        onClick = {
                            onBookMarkButtonClick()
                            isMoreClicked.value = false
                        },
                        imageVector = bookMarksVM.bookMarkIcons.value
                    )
                    DropDownMenuItemModified(
                        text = "Add to collections",
                        onClick = {
                            coroutineScope.launch {
                                bookMarksVM.loadDefaultCustomFoldersForBookMarks()
                            }
                            onBookmarkLongPress()
                            isMoreClicked.value = false
                        },
                        imageVector = Icons.Default.Add
                    )
                    DropDownMenuItemModified(
                        text = "Download",
                        onClick = {
                            isMoreClicked.value = false
                            val randomTitle = UUID.randomUUID().toString().substring(0, 6)
                            DownloadImpl(context = context).downloadNewFile(
                                url = hdImageURLForAPOD,
                                title = "$randomTitle.jpg"
                            )
                            Toast.makeText(
                                context,
                                "Downloading started, check notifications for more information",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        imageVector = Icons.Outlined.FileDownload
                    )
                }
            }

            if (!inAPODBottomSheetContent) {
                APODSideIconButton(
                    imageVector = Icons.Outlined.FileDownload,
                    onClick = {
                        val randomTitle = UUID.randomUUID().toString().substring(0, 6)
                        DownloadImpl(context = context).downloadNewFile(
                            url = hdImageURLForAPOD,
                            title = "$randomTitle.jpg"
                        )
                        Toast.makeText(
                            context,
                            "Downloading started, check notifications for more information",
                            Toast.LENGTH_SHORT
                        ).show()
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
                        .layoutId("downloadIcon"),
                    iconColor = MaterialTheme.colorScheme.onSecondary.copy(
                        apodIconTransparencyValue
                    ),
                    iconModifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary.copy(
                                apodIconButtonTransparencyValue
                            )
                        )
                        .combinedClickable(onClick = { onBookMarkButtonClick() }, onLongClick = {
                            homeScreenViewModel.currentBtmSheetType.value =
                                HomeScreenViewModel.BtmSheetType.BookMarkCollection
                            onBookmarkLongPress()
                        })
                        .layoutId("bookMarkIcon")
                ) {
                    APODSideIconButton(
                        imageVector = bookMarksVM.bookMarkIcons.value,
                        onClick = { },
                        iconBtnColor = MaterialTheme.colorScheme.secondary.copy(
                            apodIconButtonTransparencyValue
                        ),
                        iconBtnModifier = Modifier,
                        iconColor = MaterialTheme.colorScheme.onSecondary.copy(
                            apodIconTransparencyValue
                        ),
                        iconModifier = Modifier
                    )
                }

            }
        }

        "video" -> {
            WebViewModified(
                url = imageURL, modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .layoutId("apodMedia")
            )
        }
    }

}

fun Modifier.shimmer(
    visible: Boolean,
    color: Color,
    shimmerHighLightColor: Color,
): Modifier {
    return this.placeholder(
        visible = visible,
        color = color,
        shape = RoundedCornerShape(4.dp),
        highlight = PlaceholderHighlight.shimmer(highlightColor = shimmerHighLightColor)
    )
}