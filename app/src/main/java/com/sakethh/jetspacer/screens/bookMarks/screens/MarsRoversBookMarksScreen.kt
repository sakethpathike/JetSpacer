package com.sakethh.jetspacer.screens.bookMarks.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.MarsRoversDB
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarsRoversBookMarksScreen(navController: NavController) {

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.HOME_SCREEN)
        }
    }
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val bookMarksVM: BookMarksVM = viewModel()
    val bookMarksFromRoversDB = bookMarksVM.bookMarksFromRoversDB.collectAsState().value
    val roversDBDTO = MarsRoversDB().copy()
    val imgURL = remember { mutableStateOf("") }
    ModalBottomSheetLayout(
        sheetContent = {
            RoverBottomSheetContent(
                imgURL = roversDBDTO.imageURL.value,
                cameraName = roversDBDTO.capturedBy.value,
                sol = roversDBDTO.sol.value,
                earthDate = roversDBDTO.earthDate.value,
                roverName = roversDBDTO.roverName.value,
                roverStatus = roversDBDTO.roverStatus.value,
                launchingDate = roversDBDTO.launchingDate.value,
                landingDate = roversDBDTO.landingDate.value,
                onBookMarkButtonClick = {
                    imgURL.value = roversDBDTO.imageURL.value
                    triggerHapticFeedback(context = context)
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                        true
                }
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {
        if (bookMarksFromRoversDB.isEmpty()) {
            StatusScreen(
                title = "No bookmarks found",
                description = "Add images to your MARS ROVER bookmarks by clicking the bookmark icon from the MARS ROVER UI section(s) respectively",
                status = Status.BOOKMARKS_EMPTY
            )
        } else {
            LazyColumn {
                items(bookMarksFromRoversDB) { roverBookMarkedItem ->
                    APODCardComposable(
                        homeScreenViewModel = homeScreenViewModel,
                        bookMarkedCategory = Constants.SAVED_IN_ROVERS_DB,
                        inBookMarkScreen = true,
                        imageURL = roverBookMarkedItem.imageURL,
                        apodMediaType = "image",
                        imageOnClick = {
                            triggerHapticFeedback(context = context)
                            roversDBDTO.imageURL.value = roverBookMarkedItem.imageURL
                            roversDBDTO.sol.value = roverBookMarkedItem.sol
                            roversDBDTO.capturedBy.value = roverBookMarkedItem.capturedBy
                            roversDBDTO.earthDate.value = roverBookMarkedItem.earthDate
                            roversDBDTO.roverName.value = roverBookMarkedItem.roverName
                            roversDBDTO.roverStatus.value = roverBookMarkedItem.roverStatus
                            roversDBDTO.launchingDate.value = roverBookMarkedItem.launchingDate
                            roversDBDTO.landingDate.value = roverBookMarkedItem.landingDate
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        },
                        onBookMarkButtonClick = {
                            imgURL.value = roverBookMarkedItem.imageURL
                            triggerHapticFeedback(context = context)
                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                true
                        },
                        capturedOnSol = roverBookMarkedItem.sol,
                        capturedBy = roverBookMarkedItem.capturedBy,
                        roverName = roverBookMarkedItem.roverName,
                        inAPODBottomSheetContent = false,
                        onConfirmButtonClick = {
                            triggerHapticFeedback(context = context)
                            coroutineScope.launch(Dispatchers.Main) {
                                if (bookMarksVM.deleteDataFromMARSDB(imageURL = imgURL.value)) {
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
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
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