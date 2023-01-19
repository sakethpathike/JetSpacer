package com.sakethh.jetspacer.screens.bookMarks.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun APODBookMarksScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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
    val bookMarksFromAPODDB = bookMarksVM.bookMarksFromAPODDB.collectAsStateWithLifecycle().value
    val apodURL = rememberSaveable { mutableStateOf("") }
    val apodMediaType = rememberSaveable { mutableStateOf("") }
    val apodDescription = rememberSaveable { mutableStateOf("") }
    val apodTitle = rememberSaveable { mutableStateOf("") }
    val apodDate = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var didDataGetAddedInDB=false
    ModalBottomSheetLayout(
        sheetContent = {
            APODBottomSheetContent(
                homeScreenViewModel = homeScreenViewModel,
                apodURL = apodURL.value,
                apodTitle = apodTitle.value,
                apodDate = apodDate.value,
                apodDescription = apodDescription.value,
                apodMediaType = apodMediaType.value,
                onBookMarkClick = {
                    triggerHapticFeedback(context = context)
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = true
                }
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {
        if (bookMarksFromAPODDB.isEmpty()) {
            StatusScreen(
                title = "No bookmarks found",
                description = "Add images to your APOD bookmarks by clicking the bookmark icon from the APOD UI section(s) respectively",
                status = Status.BOOKMARKS_EMPTY
            )
        } else {
            LazyColumn {
                items(bookMarksFromAPODDB) { apodBookMarkedItem ->
                    APODCardComposable(
                        homeScreenViewModel = homeScreenViewModel,
                        imageURL = apodBookMarkedItem.imageURL,
                        apodDate = apodBookMarkedItem.datePublished,
                        apodDescription = apodBookMarkedItem.description,
                        apodTitle = apodBookMarkedItem.title,
                        inBookMarkScreen = true,
                        imageOnClick = {
                            apodURL.value = apodBookMarkedItem.imageURL
                            apodTitle.value = apodBookMarkedItem.title
                            apodDate.value = apodBookMarkedItem.datePublished
                            apodDescription.value = apodBookMarkedItem.description
                            apodMediaType.value = apodBookMarkedItem.mediaType
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        },
                        bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                        apodMediaType = apodBookMarkedItem.mediaType,
                        inAPODBottomSheetContent = false,
                        onBookMarkButtonClick = {
                            apodURL.value = apodBookMarkedItem.imageURL
                            triggerHapticFeedback(context = context)
                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                true
                        },
                        capturedOnSol = "",
                        capturedBy = "",
                        roverName = "",
                        onConfirmButtonClick = {
                            triggerHapticFeedback(context = context)
                            coroutineScope.launch {
                                didDataGetAddedInDB =
                                    bookMarksVM.deleteDataFromAPODDB(imageURL = apodURL.value)
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
                                bookMarksVM.doesThisExistsInAPODIconTxt(apodURL.value)
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            }
                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                false
                        }
                    )
                }

                this.item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

}