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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.CardForRowGridRaw
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.WebViewModified
import com.sakethh.jetspacer.screens.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.util.Calendar

@SuppressLint("NewApi")
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
    val apodData =spaceScreenVM.apodDateData
    val apodURL = rememberSaveable { mutableStateOf("") }
    val currentDayAPODURL=homeScreenViewModel.apodDataFromAPI.value.url.toString()
    AppTheme {
        ModalBottomSheetLayout(
            sheetContent = {
                APODBottomSheetContent(
                    homeScreenViewModel = homeScreenViewModel,
                    apodURL = apodData.value.url.toString(),
                    apodTitle = apodData.value.title.toString(),
                    apodDate = apodData.value.date.toString(),
                    apodDescription = apodData.value.explanation.toString()
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
                        imageHeight = 130.dp
                    )

                }
                item {
                    APODCardComposable(
                        homeScreenViewModel = homeScreenViewModel,
                        apodURL = apodData.value.url.toString(),
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
                            .clickable { navController.navigate(NavigationRoutes.ROVERS_SCREEN)},
                        inSpaceScreen = true,
                        imgURL = "https://ia601406.us.archive.org/18/items/jetspacer/rover%20original%20flipped%20low%20quality%20jpg.jpg",
                        imageHeight = 130.dp
                    )
                }


                item {
                    WebViewModified(
                        url = null,
                        embedString = "<iframe width=\"660\" height=\"371\" src=\"https://www.youtube.com/embed/86YLFOog4GM\" title=\"\uD83C\uDF0E Nasa Live Stream  - Earth From Space :  Live Views from the ISS\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
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
            }
        }
    }
}
