package com.sakethh.jetspacer.screens.space

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("NewApi", "SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpaceScreen(navController: NavController) {

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.HOME_SCREEN) {
                popUpTo(0)
            }
        }
    }
    val datePickerState = rememberMaterialDialogState()
    val spaceScreenVM: SpaceScreenVM = viewModel()
    val context = LocalContext.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val apodData = spaceScreenVM.apodDateData
    val apodURL = rememberSaveable { mutableStateOf("") }
    val currentDayAPODURL = homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val marsWeatherData = spaceScreenVM.marsWeatherDTO.value
    val bookMarksVM: BookMarksVM = viewModel()
    var didDataGetAddedInDB = false
    bookMarksVM.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
    AppTheme {
        ModalBottomSheetLayout(
            sheetContent = {
                APODBottomSheetContent(
                    homeScreenViewModel = homeScreenViewModel,
                    apodURL = apodData.value.url.toString(),
                    apodTitle = apodData.value.title.toString(),
                    apodDate = apodData.value.date.toString(),
                    apodDescription = apodData.value.explanation.toString(),
                    apodMediaType = apodData.value.media_type.toString(),
                    onBookMarkClick = {
                        triggerHapticFeedback(context = context)
                        bookMarksVM.imgURL = apodData.value.url.toString()
                        coroutineScope.launch {
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                            val formattedDate = dateFormat.format(Date())
                            didDataGetAddedInDB = bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                this.title = apodData.value.title.toString()
                                this.datePublished = apodData.value.date.toString()
                                this.description = apodData.value.explanation.toString()
                                this.imageURL = apodData.value.url.toString()
                                this.mediaType = "image"
                                this.isBookMarked = true
                                this.category = "APOD"
                                this.addedToLocalDBOn = formattedDate
                                this.hdImageURL = apodData.value.hdurl.toString()
                            })
                        }.invokeOnCompletion {
                            if (didDataGetAddedInDB) {
                                Toast.makeText(context, "Added to bookmarks:)", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    true
                            }
                            bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                        }
                    },
                    imageHDURL = apodData.value.hdurl.toString()
                )
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetBackgroundColor = MaterialTheme.colorScheme.primary
        ) {
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
                                        spaceScreenVM.loadData()
                                    } catch (_: Exception) {
                                        HomeScreenViewModel.Network.isConnectionSucceed.value =
                                            false
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
                    })
            Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    item {
                        CardForRowGridRaw(
                            title = "APOD",
                            value = "Browse \"Astronomy Picture Of The Day\" Archive",
                            cardModifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable { navController.navigate(NavigationRoutes.APOD_SCREEN) },
                            inSpaceScreen = true,
                            imgURL = currentDayAPODURL,
                            imageHeight = 110.dp
                        )

                    }
                    item {
                        bookMarksVM.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
                        APODCardComposable(
                            homeScreenViewModel = homeScreenViewModel,
                            imageURL = apodData.value.url.toString(),
                            apodTitle = apodData.value.title.toString(),
                            apodDate = apodData.value.date.toString(),
                            apodDescription = apodData.value.explanation.toString(),
                            imageOnClick = {
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }.invokeOnCompletion {
                                    bookMarksVM.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
                                }
                            },
                            cardTopPaddingValue = 0.dp,
                            inSpaceScreen = true,
                            changeDateChipOnClick = {
                                datePickerState.show()
                            },
                            apodMediaType = apodData.value.media_type.toString(),
                            inAPODBottomSheetContent = false,
                            onBookMarkButtonClick = {
                                bookMarksVM.imgURL = apodData.value.url.toString()
                                coroutineScope.launch {
                                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                                    val formattedDate = dateFormat.format(Date())
                                    didDataGetAddedInDB =
                                        bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                            this.title = apodData.value.title.toString()
                                            this.datePublished = apodData.value.date.toString()
                                            this.description = apodData.value.explanation.toString()
                                            this.imageURL = apodData.value.url.toString()
                                            this.mediaType = "image"
                                            this.isBookMarked = true
                                            this.category = "APOD"
                                            this.addedToLocalDBOn = formattedDate
                                            this.hdImageURL = apodData.value.hdurl.toString()
                                        })
                                }.invokeOnCompletion {
                                    if (didDataGetAddedInDB) {
                                        Toast.makeText(
                                            context,
                                            "Added to bookmarks:)",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                            true
                                    }
                                    bookMarksVM.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
                                }
                            },
                            capturedOnSol = "",
                            capturedBy = "",
                            roverName = "",
                            onConfirmButtonClick = {
                                triggerHapticFeedback(context = context)
                                coroutineScope.launch {
                                    didDataGetAddedInDB =
                                        bookMarksVM.deleteDataFromAPODDB(imageURL = bookMarksVM.imgURL)
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
                                    bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                                }
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    false
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                    false
                            },
                            apodHDImageURL = apodData.value.hdurl.toString()
                        )
                    }
                    item {
                        CardForRowGridRaw(
                            title = "Mars Rovers",
                            value = "Browse images captured by \"Mars Rovers\" straight from the Mars",
                            cardModifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable { navController.navigate(NavigationRoutes.ROVERS_SCREEN) },
                            inSpaceScreen = true,
                            imgURL = "https://ia601406.us.archive.org/18/items/jetspacer/rover%20original%20flipped%20low%20quality%20jpg.jpg",
                            imageHeight = 125.dp
                        )
                    }
                    item {
                        androidx.compose.material3.Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, top = 0.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            ConstraintLayout(constraintSet = constraintSet) {
                                Icon(
                                    imageVector = Icons.Outlined.Insights,
                                    contentDescription = "Icon Of \"Insights\"",
                                    modifier = Modifier
                                        .padding(top = 15.dp, start = 15.dp)
                                        .size(25.dp)
                                        .layoutId("cardIcon"),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Weather on the Red Planet",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier
                                        .padding(start = 10.dp, top = 10.dp)
                                        .layoutId("cardTitle")
                                )
                                Text(
                                    text = "Based on Curiosity Rover on Mars",
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
                            CardRowGrid(
                                lhsCardTitle = "Terrestrial Date",
                                lhsCardValue = marsWeatherData.terrestrial_date,
                                isLHSShimmerVisible = marsWeatherData.terrestrial_date.isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Season",
                                rhsCardValue = marsWeatherData.season,
                                isRHSShimmerVisible = marsWeatherData.season.isEmpty(),
                                isLHSShimmering = marsWeatherData.terrestrial_date.isEmpty(),
                                isRHSShimmering = marsWeatherData.season.isEmpty(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Min Temperature",
                                lhsCardValue = marsWeatherData.min_temp.toString(),
                                isLHSShimmerVisible = marsWeatherData.min_temp == 0,
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Max Temperature",
                                rhsCardValue = marsWeatherData.max_temp.toString(),
                                isRHSShimmerVisible = marsWeatherData.max_temp == 0,
                                isLHSShimmering = marsWeatherData.min_temp == 0,
                                isRHSShimmering = marsWeatherData.max_temp == 0,
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Pressure",
                                lhsCardValue = marsWeatherData.pressure.toString(),
                                isLHSShimmerVisible = marsWeatherData.pressure == 0,
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Pressure String",
                                rhsCardValue = marsWeatherData.pressure_string,
                                isRHSShimmerVisible = marsWeatherData.pressure_string.isBlank(),
                                isLHSShimmering = marsWeatherData.pressure == 0,
                                isRHSShimmering = marsWeatherData.pressure_string.isBlank(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,

                                )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Atmosphere",
                                lhsCardValue = marsWeatherData.atmo_opacity,
                                isLHSShimmerVisible = marsWeatherData.atmo_opacity.toString()
                                    .isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Local UV Irradiance",
                                rhsCardValue = marsWeatherData.local_uv_irradiance_index,
                                isRHSShimmerVisible = marsWeatherData.local_uv_irradiance_index.isBlank(),
                                isLHSShimmering = marsWeatherData.atmo_opacity.toString().isEmpty(),
                                isRHSShimmering = marsWeatherData.local_uv_irradiance_index.isBlank(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,

                                )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Sunrise",
                                lhsCardValue = marsWeatherData.sunrise,
                                isLHSShimmerVisible = marsWeatherData.sunrise.toString().isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Sunset",
                                rhsCardValue = marsWeatherData.sunset,
                                isRHSShimmerVisible = marsWeatherData.sunset.isBlank(),
                                isLHSShimmering = marsWeatherData.sunrise.toString().isEmpty(),
                                isRHSShimmering = marsWeatherData.sunset.isBlank(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,

                                )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Min GTS Temperature",
                                lhsCardValue = marsWeatherData.min_gts_temp.toString(),
                                isLHSShimmerVisible = marsWeatherData.min_gts_temp == 0,
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                rhsCardTitle = "Max GTS Temperature",
                                rhsCardValue = marsWeatherData.max_gts_temp.toString(),
                                isRHSShimmerVisible = marsWeatherData.max_gts_temp.toString()
                                    .isBlank(),
                                isLHSShimmering = marsWeatherData.min_gts_temp == 0,
                                isRHSShimmering = marsWeatherData.max_gts_temp.toString().isBlank(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Unit Of Measure",
                                lhsCardValue = marsWeatherData.unitOfMeasure,
                                isLHSShimmerVisible = marsWeatherData.unitOfMeasure.toString()
                                    .isEmpty(),
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isRHSShimmerVisible = marsWeatherData.TZ_Data.isBlank(),
                                isLHSShimmering = marsWeatherData.unitOfMeasure.toString()
                                    .isEmpty(),
                                isRHSShimmering = marsWeatherData.TZ_Data.isBlank(),
                                rhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                rhsCardTitle = "Timezone",
                                rhsCardValue = marsWeatherData.TZ_Data
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CardRowGrid(
                                lhsCardTitle = "Sol",
                                lhsCardValue = marsWeatherData.sol.toString(),
                                isLHSShimmerVisible = marsWeatherData.sol == 0,
                                lhsShimmerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                lhsShimmerHighlightColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                isLHSShimmering = marsWeatherData.sol == 0,
                                rhsCardTitle = "",
                                rhsCardValue = "",
                                rhsCardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(75.dp))
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing.value,
                    state = pullRefreshState,
                    Modifier.align(Alignment.TopCenter),
                    scale = true
                )
            }
        }
        val customApodDate = remember { mutableStateOf("") }
        val dateFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd")
        MaterialDialog(backgroundColor=MaterialTheme.colorScheme.primary,shape = RoundedCornerShape(10.dp), dialogState = datePickerState, buttons = {
            positiveButton(text = "Change date NOW", onClick = {
                triggerHapticFeedback(context = context)
                datePickerState.hide()
                coroutineScope.launch {
                    spaceScreenVM.getAPODDateData(customApodDate.value)
                }
            })
            negativeButton(text = "Never mind", onClick = { datePickerState.hide() })
        }) {
            datepicker(
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    headerTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    calendarHeaderTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    dateActiveBackgroundColor=MaterialTheme.colorScheme.onPrimary,
                    dateInactiveBackgroundColor= Color.Transparent,
                    dateActiveTextColor=MaterialTheme.colorScheme.primary,
                    dateInactiveTextColor=MaterialTheme.colorScheme.onPrimary
                ),
                initialDate = LocalDate.now().minusDays(2),
                title = "Pick a date",
                yearRange = 1995..LocalDate.now().year
            ){
                customApodDate.value=it.format(dateFormat).toString()
            }
        }

        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        didDataGetAddedInDB =
                            bookMarksVM.deleteDataFromAPODDB(imageURL = bookMarksVM.imgURL)
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
                        bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                }
            )
        }
    }
}

/*
androidx.compose.material3.AlertDialog(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(20.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onDismissRequest = {
                    isDatePickerAlertDialogEnabled.value = false
                }, content = {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        androidx.compose.material3.Text(
                            text = "Pick a date!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        WheelDatePicker(
                            minDate = LocalDate.of(1995, 6, 16),
                            maxDate = LocalDate.parse(homeScreenViewModel.apodDataFromAPI.value.date.toString()),
                            textStyle = MaterialTheme.typography.headlineMedium,
                            textColor = MaterialTheme.colorScheme.onSurface
                        ) { selectedDate ->
                            apodURL.value =
                                "${selectedDate.year}-${selectedDate.monthValue}-${selectedDate.dayOfMonth}"
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        androidx.compose.material3.OutlinedButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                isDatePickerAlertDialogEnabled.value =
                                    false
                            },
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            androidx.compose.material3.Text(
                                text = "Not really(¬_¬ )",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        androidx.compose.material3.Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                isDatePickerAlertDialogEnabled.value = false

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                        ) {
                            androidx.compose.material3.Text(
                                text = "Change date NOW!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                })

* */