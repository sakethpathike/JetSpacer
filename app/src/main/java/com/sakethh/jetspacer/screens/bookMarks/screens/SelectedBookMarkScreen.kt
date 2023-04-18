package com.sakethh.jetspacer.screens.bookMarks.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.triggerHapticFeedback
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContent
import com.sakethh.jetspacer.screens.news.NewsVM
import com.sakethh.jetspacer.screens.news.newsUI
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import com.sakethh.jetspacer.screens.webview.WebViewUtils.newsBottomSheetContentImpl
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SelectedBookMarkScreen(navController: NavController) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = false
        )
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if (modalBottomSheetState.isVisible) {
            coroutineScope.launch {
                modalBottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN) {
                popUpTo(0)
            }
        }
    }
    val roversScreenVM: RoversScreenVM = viewModel()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBookMarkScreenVM: SelectedBookMarkScreenVM = viewModel()
    val selectedData =
        selectedBookMarkScreenVM.selectedBookMarkData.collectAsState().value
    val bookMarksVM: BookMarksVM = viewModel()
    val apodData =
        bookMarksVM.localDB.getBookMarkedAPODDBDATA().collectAsState(initial = emptyList()).value
    val newsData =
        bookMarksVM.localDB.getBookMarkedNewsDATA().collectAsState(initial = emptyList())
    val roversData =
        bookMarksVM.localDB.getBookMarkedRoverDBDATA().collectAsState(initial = emptyList()).value
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    var didDataGetAddedInDB = false
    AppTheme {
        ModalBottomSheetLayout(
            sheetContent = {
                Spacer(modifier = Modifier.size(1.dp))
                when (selectedBookMarkScreenVM.selectedDataType.value) {
                    SavedDataType.APOD -> {
                        bookMarksVM.doesThisExistsInAPODIconTxt(selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value.toString())
                        APODBottomSheetContent(
                            inCustomBookmarkScreen = true,
                            homeScreenViewModel = homeScreenViewModel,
                            apodURL = selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value,
                            apodTitle = selectedBookMarkScreenVM.apodBtmSheetData.apodTitle.value,
                            apodDate = selectedBookMarkScreenVM.apodBtmSheetData.apodDate.value,
                            apodDescription = selectedBookMarkScreenVM.apodBtmSheetData.apodDescription.value,
                            apodMediaType = selectedBookMarkScreenVM.apodBtmSheetData.apodMediaType.value,
                            onBookMarkClick = {
                                triggerHapticFeedback(context = context)
                                bookMarksVM.imgURL =
                                    selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value
                                coroutineScope.launch {
                                    val formattedDate = dateFormat.format(Date())
                                    didDataGetAddedInDB =
                                        bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                            this.title =
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodTitle.value
                                            this.datePublished =
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodDate.value
                                            this.description =
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodDescription.value
                                            this.imageURL =
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value
                                            this.mediaType = "image"
                                            this.isBookMarked = true
                                            this.category = "APOD"
                                            this.hdImageURL =
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodHDURL.value
                                            addedToLocalDBOn = formattedDate
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
                            imageHDURL = homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                        )

                    }

                    SavedDataType.NEWS -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            NewsBottomSheetContent(newsBottomSheetMutableStateDTO = NewsVM.newsBottomSheetContentImpl.value)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    SavedDataType.ROVERS -> {
                        bookMarksVM.doesThisExistsInRoverDBIconTxt(roversScreenVM.imgURL.value)
                        RoverBottomSheetContent(
                            imgURL = roversScreenVM.imgURL.value,
                            cameraName = roversScreenVM.cameraName.value,
                            sol = roversScreenVM.sol.value,
                            earthDate = roversScreenVM.earthDate.value,
                            roverName = roversScreenVM.roverName.value,
                            roverStatus = roversScreenVM.roverStatus.value,
                            launchingDate = roversScreenVM.launchingDate.value,
                            landingDate = roversScreenVM.landingDate.value,
                            onBookMarkButtonClick = {
                                triggerHapticFeedback(context = context)
                                bookMarksVM.imgURL = roversScreenVM.imgURL.value
                                val marsRoverData = MarsRoversDBDTO().apply {
                                    this.imageURL = roversScreenVM.imgURL.value
                                    this.capturedBy = roversScreenVM.cameraName.value
                                    this.sol = roversScreenVM.sol.value
                                    this.earthDate = roversScreenVM.earthDate.value
                                    this.roverName = roversScreenVM.roverName.value
                                    this.roverStatus = roversScreenVM.roverStatus.value
                                    this.launchingDate = roversScreenVM.launchingDate.value
                                    this.landingDate = roversScreenVM.landingDate.value
                                    this.isBookMarked = true
                                    this.category = "Rover"
                                    this.addedToLocalDBOn = currentDate
                                }
                                coroutineScope.launch {
                                    didDataGetAddedInDB =
                                        bookMarksVM.addDataToMarsDB(marsRoversDBDTO = marsRoverData)
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
                                    bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                                }
                                bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                            },
                            onConfirmBookMarkDeletionButtonClick = {
                                coroutineScope.launch {
                                    didDataGetAddedInDB =
                                        bookMarksVM.deleteDataFromMARSDB(bookMarksVM.imgURL)
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
                                    bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                                }
                            }
                        )
                    }

                    else -> {

                    }
                }
            },
            sheetState = modalBottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetBackgroundColor = MaterialTheme.colorScheme.primary
        ) {
            Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.surface), topBar = {
                TopAppBar(scrollBehavior = scrollBehavior, title = {
                    Text(
                        text = selectedData.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, end = 15.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
            }) {
                when (selectedData.name) {
                    "APOD" -> {
                        selectedBookMarkScreenVM.selectedDataType.value = SavedDataType.APOD
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.padding(it),
                            columns = StaggeredGridCells.Adaptive(150.dp)
                        ) {
                            items(apodData) { element ->
                                Coil_Image().CoilImage(
                                    imgURL = element.imageURL,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .clickable {
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value =
                                                element.imageURL
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodDate.value =
                                                element.datePublished
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodHDURL.value =
                                                element.hdImageURL
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodDescription.value =
                                                element.description
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodMediaType.value =
                                                element.mediaType
                                            selectedBookMarkScreenVM.apodBtmSheetData.apodTitle.value =
                                                element.title
                                            coroutineScope.launch {
                                                modalBottomSheetState.show()
                                            }
                                        },
                                    onError = painterResource(id = R.drawable.baseline_image_24),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                    }

                    "News" -> {
                        selectedBookMarkScreenVM.selectedDataType.value = SavedDataType.NEWS
                        LazyColumn(
                            contentPadding = it,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            this.newsUI(
                                bookMarkedData = newsData,
                                bottomSheetState = modalBottomSheetState,
                                coroutineScope = coroutineScope,
                                newsBottomSheetContentImpl = NewsVM.newsBottomSheetContentImpl.value,
                                navController = navController,
                                bookMarksVM = bookMarksVM
                            )
                        }
                    }

                    "Rovers" -> {
                        selectedBookMarkScreenVM.selectedDataType.value = SavedDataType.ROVERS
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.padding(it),
                            columns = StaggeredGridCells.Adaptive(150.dp)
                        ) {
                            items(roversData) { element ->
                                Coil_Image().CoilImage(
                                    imgURL = element.imageURL,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .clickable {
                                            roversScreenVM.imgURL.value = element.imageURL
                                            roversScreenVM.cameraName.value = element.capturedBy
                                            roversScreenVM.sol.value = element.sol
                                            roversScreenVM.earthDate.value = element.earthDate
                                            roversScreenVM.roverName.value = element.roverName
                                            roversScreenVM.roverStatus.value = element.roverStatus
                                            roversScreenVM.launchingDate.value =
                                                element.launchingDate
                                            roversScreenVM.landingDate.value = element.landingDate
                                            coroutineScope.launch {
                                                modalBottomSheetState.show()
                                            }
                                        },
                                    onError = painterResource(id = R.drawable.baseline_image_24),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    else -> {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.padding(it),
                            columns = StaggeredGridCells.Adaptive(150.dp)
                        ) {
                            itemsIndexed(selectedData.data) { index, element ->
                                val imgURL: String =
                                    when (selectedData.data[index].dataType) {
                                        SavedDataType.APOD -> {
                                            element.data as APOD_DB_DTO
                                            element.data.imageURL
                                        }

                                        SavedDataType.ROVERS -> {
                                            element.data as MarsRoversDBDTO
                                            element.data.imageURL
                                        }

                                        SavedDataType.NEWS -> {
                                            element.data as NewsDB
                                            element.data.imageURL
                                        }

                                        else -> {
                                            ""
                                        }
                                    }
                                Coil_Image().CoilImage(
                                    modifier = Modifier.clickable {
                                        when (element.dataType) {
                                            SavedDataType.APOD -> {
                                                selectedBookMarkScreenVM.selectedDataType.value =
                                                    SavedDataType.APOD
                                                element.data as APOD_DB_DTO
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value =
                                                    element.data.imageURL
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodTitle.value =
                                                    element.data.title
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodDate.value =
                                                    element.data.datePublished
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodDescription.value =
                                                    element.data.description
                                                selectedBookMarkScreenVM.apodBtmSheetData.apodHDURL.value =
                                                    element.data.hdImageURL
                                                coroutineScope.launch {
                                                    modalBottomSheetState.show()
                                                }
                                            }

                                            SavedDataType.NEWS -> {
                                                selectedBookMarkScreenVM.selectedDataType.value =
                                                    SavedDataType.NEWS
                                                element.data as NewsDB
                                                NewsVM.newsBottomSheetContentImpl.value.imageURL.value =
                                                    element.data.imageURL
                                                NewsVM.newsBottomSheetContentImpl.value.sourceURL.value =
                                                    element.data.sourceURL
                                                NewsVM.newsBottomSheetContentImpl.value.publishedTime.value =
                                                    element.data.publishedTime
                                                NewsVM.newsBottomSheetContentImpl.value.title.value =
                                                    element.data.title
                                                NewsVM.newsBottomSheetContentImpl.value.sourceName.value =
                                                    element.data.sourceOfNews

                                                coroutineScope.launch {
                                                    modalBottomSheetState.show()
                                                }
                                            }

                                            SavedDataType.ROVERS -> {
                                                selectedBookMarkScreenVM.selectedDataType.value =
                                                    SavedDataType.ROVERS
                                                element.data as MarsRoversDBDTO
                                                roversScreenVM.imgURL.value = element.data.imageURL
                                                roversScreenVM.cameraName.value =
                                                    element.data.capturedBy
                                                roversScreenVM.sol.value = element.data.sol
                                                roversScreenVM.earthDate.value =
                                                    element.data.earthDate
                                                roversScreenVM.roverName.value =
                                                    element.data.roverName
                                                roversScreenVM.roverStatus.value =
                                                    element.data.roverStatus
                                                roversScreenVM.launchingDate.value =
                                                    element.data.launchingDate
                                                roversScreenVM.landingDate.value =
                                                    element.data.landingDate
                                            }

                                            else -> {

                                            }
                                        }
                                    },
                                    imgURL = imgURL,
                                    contentDescription = "",
                                    onError = painterResource(id = R.drawable.baseline_image_24),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            when (selectedData.name) {
                "APOD" -> {
                    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
                        AlertDialogForDeletingFromDB(
                            bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                            onConfirmBtnClick = {
                                triggerHapticFeedback(context = context)
                                coroutineScope.launch {
                                    didDataGetAddedInDB =
                                        bookMarksVM.deleteDataFromAPODDB(imageURL = selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value)
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
                                    bookMarksVM.doesThisExistsInAPODIconTxt(selectedBookMarkScreenVM.apodBtmSheetData.apodURL.value)
                                }
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    false
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                    false
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                }.invokeOnCompletion {
                                    if (apodData.isEmpty()) {
                                        navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN)
                                    }
                                }
                            }
                        )
                    }
                }

                "News" -> {
                    var doesExistsInDB = false
                    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
                        AlertDialogForDeletingFromDB(
                            bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                            onConfirmBtnClick = {
                                triggerHapticFeedback(context = context)
                                coroutineScope.launch {
                                    doesExistsInDB =
                                        bookMarksVM.deleteDataFromNewsDB(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                                    modalBottomSheetState.hide()
                                }.invokeOnCompletion {
                                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                                    if (doesExistsInDB) {
                                        Toast.makeText(
                                            context,
                                            "Bookmark didn't got removed as expected, report it:(",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                                        Toast.makeText(
                                            context,
                                            "Removed from bookmarks:)",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                                }
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    false
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                    false
                                if (newsData.value.isEmpty()) {
                                    navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN)
                                }
                            }
                        )
                    }
                }

                "Rovers" -> {
                    var doesExistsInDB = false
                    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
                        AlertDialogForDeletingFromDB(
                            bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
                            onConfirmBtnClick = {
                                triggerHapticFeedback(context = context)
                                coroutineScope.launch {
                                    doesExistsInDB =
                                        bookMarksVM.deleteDataFromMARSDB(imageURL = bookMarksVM.imgURL)
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
                                        ).show()
                                    }
                                    bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                                    coroutineScope.launch {
                                        modalBottomSheetState.hide()
                                    }.invokeOnCompletion {
                                        if (roversData.isEmpty()) {
                                            navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN)
                                        }
                                    }
                                }
                                bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    false
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                    false
                            }
                        )
                    }
                }
            }
        }
    }
}