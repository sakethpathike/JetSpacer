package com.sakethh.jetspacer.screens.space

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("NewApi", "SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
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

    val spaceScreenVM: SpaceScreenVM = viewModel()
    val context = LocalContext.current
    val isDatePickerAlertDialogEnabled = rememberSaveable { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val apodData = spaceScreenVM.apodDateData
    val apodURL = rememberSaveable { mutableStateOf("") }
    val currentDayAPODURL = homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val marsWeatherData = spaceScreenVM.marsWeatherDTO.value
    val bookMarksVM:BookMarksVM= viewModel()
    var didDataGetAddedInDB=false
    homeScreenViewModel.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
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
                            })
                        }.invokeOnCompletion {
                            if (didDataGetAddedInDB) {
                                Toast.makeText(context, "Added to bookmarks:)", Toast.LENGTH_SHORT).show()
                            } else {
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value
                            }
                        }
                    }
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
                    APODCardComposable(
                        homeScreenViewModel = homeScreenViewModel,
                        imageURL = apodData.value.url.toString(),
                        apodTitle = apodData.value.title.toString(),
                        apodDate = apodData.value.date.toString(),
                        apodDescription = apodData.value.explanation.toString(),
                        imageOnClick = {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        },
                        cardTopPaddingValue = 0.dp,
                        inSpaceScreen = true,
                        changeDateChipOnClick = {
                            isDatePickerAlertDialogEnabled.value = true
                        },
                        apodMediaType = apodData.value.media_type.toString(),
                        inAPODBottomSheetContent = false,
                        onBookMarkButtonClick = {
                            homeScreenViewModel.doesThisExistsInAPODIconTxt(apodData.value.url.toString())
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
                                })
                            }.invokeOnCompletion {
                                if (didDataGetAddedInDB) {
                                    Toast.makeText(context, "Added to bookmarks:)", Toast.LENGTH_SHORT).show()
                                } else {
                                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value
                                }
                            }
                        },
                        capturedOnSol = "",
                        capturedBy = "",
                        roverName = "",
                        onConfirmButtonClick = {
                            triggerHapticFeedback(context = context)
                            coroutineScope.launch(Dispatchers.Main) {
                                if (bookMarksVM.deleteDataFromAPODDB(imageURL = apodData.value.url.toString())) {
                                    Toast.makeText(
                                        context,
                                        "Removed from bookmarks:)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                false
                        }
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
                    if (isDatePickerAlertDialogEnabled.value) {
                        androidx.compose.material3.AlertDialog(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            onDismissRequest = {
                                isDatePickerAlertDialogEnabled.value = false
                            },
                            title = {
                                androidx.compose.material3.Text(
                                    text = "Pick a date!",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontSize = 18.sp
                                )
                            }, text = {
                                WheelDatePicker(
                                    minYear = 1995,
                                    maxYear = currentYear,
                                    textStyle = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                                    textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                                ) { selectedDate ->
                                    apodURL.value =
                                        "${selectedDate.year}-${selectedDate.monthValue}-${selectedDate.dayOfMonth}"
                                }
                            }, confirmButton = {
                                androidx.compose.material3.Button(
                                    onClick = {
                                        isDatePickerAlertDialogEnabled.value =
                                            false
                                        coroutineScope.launch {
                                            spaceScreenVM.getAPODDateData(apodURL.value)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    androidx.compose.material3.Text(
                                        text = "Change date NOW!",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }, dismissButton = {
                                androidx.compose.material3.OutlinedButton(
                                    onClick = {
                                        isDatePickerAlertDialogEnabled.value =
                                            false
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    androidx.compose.material3.Text(
                                        text = "Not really(¬_¬ )",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            })
                    }
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
                            rhsCardTitle = "Season",
                            rhsCardValue = marsWeatherData.season
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Min Temperature",
                            lhsCardValue = marsWeatherData.min_temp.toString(),
                            rhsCardTitle = "Max Temperature",
                            rhsCardValue = marsWeatherData.max_temp.toString()
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Pressure",
                            lhsCardValue = marsWeatherData.pressure.toString(),
                            rhsCardTitle = "Pressure String",
                            rhsCardValue = marsWeatherData.pressure_string
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Atmosphere",
                            lhsCardValue = marsWeatherData.atmo_opacity,
                            rhsCardTitle = "Local UV Irradiance",
                            rhsCardValue = marsWeatherData.local_uv_irradiance_index
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Sunrise",
                            lhsCardValue = marsWeatherData.sunrise,
                            rhsCardTitle = "Sunset",
                            rhsCardValue = marsWeatherData.sunset
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Min GTS Temperature",
                            lhsCardValue = marsWeatherData.min_gts_temp.toString(),
                            rhsCardTitle = "Max GTS Temperature",
                            rhsCardValue = marsWeatherData.max_gts_temp.toString()
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Unit Of Measure",
                            lhsCardValue = marsWeatherData.unitOfMeasure,
                            rhsCardTitle = "Timezone",
                            rhsCardValue = marsWeatherData.TZ_Data
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CardRowGrid(
                            lhsCardTitle = "Sol",
                            lhsCardValue = marsWeatherData.sol.toString(),
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
        }
    }
}
