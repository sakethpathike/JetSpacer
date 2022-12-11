package com.sakethh.jetspacer.screens.bookMarks.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.MarsRoversDB
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarsRoversBookMarksScreen(navController: NavController) {

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

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
    ModalBottomSheetLayout(
        sheetContent = {
            RoverBottomSheetContent(
                imgURL = roversDBDTO.imageURL.value,
                capturedOn = roversDBDTO.sol.value,
                cameraName = roversDBDTO.capturedBy.value,
                sol = roversDBDTO.sol.value,
                earthDate = roversDBDTO.earthDate.value,
                roverName = roversDBDTO.roverName.value,
                roverStatus = roversDBDTO.roverStatus.value,
                launchingDate = roversDBDTO.launchingDate.value,
                landingDate = roversDBDTO.landingDate.value
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.primary
    ) {
        LazyColumn {
            items(bookMarksFromRoversDB) { roverBookMarkedItem ->
                APODCardComposable(
                    homeScreenViewModel = homeScreenViewModel,
                    bookMarkedCategory = Constants.SAVED_IN_ROVERS_DB,
                    inBookMarkScreen = true,
                    imageURL = roverBookMarkedItem.imageURL,
                    apodMediaType = "image",
                    roverDBDTO = MarsRoversDBDTO().apply {
                        this.addedToLocalDBOn = roverBookMarkedItem.addedToLocalDBOn
                        this.capturedBy = roverBookMarkedItem.capturedBy
                        this.category = Constants.SAVED_IN_ROVERS_DB
                        this.earthDate = roverBookMarkedItem.earthDate
                        this.roverStatus = roverBookMarkedItem.roverStatus
                        this.roverName = roverBookMarkedItem.roverName
                        this.id = roverBookMarkedItem.imageURL
                        this.imageURL = roverBookMarkedItem.imageURL
                        this.isBookMarked = true
                        this.landingDate = roverBookMarkedItem.landingDate
                        this.launchingDate = roverBookMarkedItem.launchingDate
                        this.sol = roverBookMarkedItem.sol
                    },
                    imageOnClick = {
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
                    saveToAPODDB = false,
                    saveToMarsRoversDB = true,
                    inAPODBottomSheetContent = false
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

}