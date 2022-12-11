package com.sakethh.jetspacer.screens.bookMarks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun BookMarksScreen(navController: NavController) {
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val systemUIController = rememberSystemUiController()
    systemUIController.setStatusBarColor(MaterialTheme.colorScheme.surface)
    systemUIController.setNavigationBarColor(MaterialTheme.colorScheme.primaryContainer)


    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val bookMarksVM: BookMarksVM = viewModel()


    val selectedChipIndex = rememberSaveable { mutableStateOf(0) }

    val apodURL = rememberSaveable { mutableStateOf("") }
    val apodTitle = rememberSaveable { mutableStateOf("") }
    val apodDate = rememberSaveable { mutableStateOf("") }
    val apodDescription = rememberSaveable { mutableStateOf("") }
    val apodMediaType = rememberSaveable { mutableStateOf("") }

    AppTheme {
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
            Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                item {
                    Text(
                        text = "Bookmarks",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(top = 30.dp, start = 15.dp)
                    )
                }
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        bookMarksVM.bookMarksScreensData.forEachIndexed { index, bookMarksScreensData ->
                            FilterChip(
                                modifier = Modifier.padding(end = 10.dp),
                                selected = selectedChipIndex.value == index,
                                onClick = {
                                    selectedChipIndex.value = index
                                },
                                label = {
                                    Text(
                                        text = bookMarksScreensData.screenName,
                                        color = if(selectedChipIndex.value == index)MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                },
                                border = FilterChipDefaults.filterChipBorder(
                                    selectedBorderColor = MaterialTheme.colorScheme.primary,
                                    borderWidth = 1.dp
                                ),
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
            bookMarksVM.bookMarksScreensData[selectedChipIndex.value].screenComposable(navController)
            }
        }

    }
}

//items(bookMarksFromAPODDB) { apodBookMarkedItem ->
//                        APODCardComposable(
//                            homeScreenViewModel = homeScreenViewModel,
//                            imageURL = apodBookMarkedItem.imageURL,
//                            apodDate = apodBookMarkedItem.datePublished,
//                            apodDescription = apodBookMarkedItem.description,
//                            apodTitle = apodBookMarkedItem.title,
//                            inBookMarkScreen = true,
//                            imageOnClick = {
//                                apodURL.value = apodBookMarkedItem.imageURL
//                                apodTitle.value = apodBookMarkedItem.title
//                                apodDate.value = apodBookMarkedItem.datePublished
//                                apodDescription.value = apodBookMarkedItem.description
//                                apodMediaType.value = apodBookMarkedItem.mediaType
//                                coroutineScope.launch {
//                                    bottomSheetState.show()
//                                }
//                            },
//                            bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
//                            apodMediaType = apodBookMarkedItem.mediaType,
//                            saveToMarsRoversDB = false,
//                            saveToAPODDB = true,
//                            inAPODBottomSheetContent = false,
//                            roverDBDTO = null
//                        )
//                    }
//                    items(bookMarksFromRoversDB) { roverBookMarkedItem ->
//                        APODCardComposable(
//                            homeScreenViewModel = homeScreenViewModel,
//                            bookMarkedCategory = Constants.SAVED_IN_ROVERS_DB,
//                            inBookMarkScreen = true,
//                            imageURL = roverBookMarkedItem.imageURL,
//                            roverDBDTO = MarsRoversDBDTO().apply {
//                                this.addedToLocalDBOn = roverBookMarkedItem.addedToLocalDBOn
//                                this.capturedBy = roverBookMarkedItem.capturedBy
//                                this.category = Constants.SAVED_IN_ROVERS_DB
//                                this.earthDate = roverBookMarkedItem.earthDate
//                                this.roverStatus = roverBookMarkedItem.roverStatus
//                                this.roverName = roverBookMarkedItem.roverName
//                                this.id = roverBookMarkedItem.imageURL
//                                this.imageURL = roverBookMarkedItem.imageURL
//                                this.isBookMarked = true
//                                this.landingDate = roverBookMarkedItem.landingDate
//                                this.launchingDate = roverBookMarkedItem.launchingDate
//                                this.sol = roverBookMarkedItem.sol
//                            },
//                            saveToAPODDB = false,
//                            saveToMarsRoversDB = true,
//                            inAPODBottomSheetContent = false
//                        )
//                    }