package com.sakethh.jetspacer.screens.bookMarks.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val bookMarksFromAPODDB = bookMarksVM.bookMarksFromAPODDB.collectAsState().value
    val apodURL = remember { mutableStateOf("") }
    val apodMediaType = remember { mutableStateOf("") }
    val apodDescription = remember { mutableStateOf("") }
    val apodTitle = remember { mutableStateOf("") }
    val apodDate = remember { mutableStateOf("") }
    ModalBottomSheetLayout(
        sheetContent = {
            APODBottomSheetContent(
                homeScreenViewModel = homeScreenViewModel,
                apodURL = apodURL.value,
                apodTitle = apodTitle.value,
                apodDate = apodDate.value,
                apodDescription = apodDescription.value,
                apodMediaType = apodMediaType.value
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {
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
                    saveToMarsRoversDB = false,
                    saveToAPODDB = true,
                    inAPODBottomSheetContent = false,
                    roverDBDTO = null
                )
            }

            this.item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}