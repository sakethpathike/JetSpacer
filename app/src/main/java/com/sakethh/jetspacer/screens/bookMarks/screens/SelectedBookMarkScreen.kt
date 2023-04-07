package com.sakethh.jetspacer.screens.bookMarks.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.triggerHapticFeedback
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContent
import com.sakethh.jetspacer.screens.news.newsUI
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import com.sakethh.jetspacer.screens.webview.WebViewUtils.newsBottomSheetContentImpl
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SelectedBookMarkScreen(navController: NavController) {
    BackHandler {
        navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN) {
            popUpTo(0)
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
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val apodURL = homeScreenViewModel.apodDataFromAPI.value.url.toString()
    val apodTitle = homeScreenViewModel.apodDataFromAPI.value.title.toString()
    val apodDescription = homeScreenViewModel.apodDataFromAPI.value.explanation.toString()
    val apodDate = homeScreenViewModel.apodDataFromAPI.value.date.toString()
    val apodMediaType = homeScreenViewModel.apodDataFromAPI.value.media_type.toString()
    var didDataGetAddedInDB = false
    AppTheme {
        ModalBottomSheetLayout(
            sheetContent = {
                Spacer(modifier = Modifier.size(1.dp))
                when (selectedData.name) {
                    "APOD" -> {
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
                            imageHDURL = homeScreenViewModel.apodDataFromAPI.value.hdurl.toString()
                        )

                    }
                    "News" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            NewsBottomSheetContent(newsBottomSheetContentImpl = newsBottomSheetContentImpl)
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                    "Rovers" -> {
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
                        LazyVerticalGrid(
                            modifier = Modifier.padding(it),
                            columns = GridCells.Adaptive(120.dp)
                        ) {
                            items(apodData) { element ->
                                Coil_Image().CoilImage(
                                    imgURL = element.imageURL,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .height(120.dp)
                                        .clickable {
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
                                newsBottomSheetContentImpl = newsBottomSheetContentImpl,
                                navController = navController,
                                bookMarksVM = bookMarksVM
                            )
                        }
                    }
                    "Rovers" -> {
                        LazyVerticalGrid(
                            modifier = Modifier.padding(it),
                            columns = GridCells.Adaptive(120.dp)
                        ) {
                            items(roversData) { element ->
                                Coil_Image().CoilImage(
                                    imgURL = element.imageURL,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .height(120.dp)
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
                        LazyVerticalGrid(
                            modifier = Modifier.padding(it),
                            columns = GridCells.Adaptive(120.dp)
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
                                    imgURL = imgURL,
                                    contentDescription = "",
                                    modifier = Modifier.height(135.dp),
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
                "News" -> {
                    var doesExistsInDB = false
                    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
                        AlertDialogForDeletingFromDB(
                            bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                            onConfirmBtnClick = {
                                triggerHapticFeedback(context = context)
                                coroutineScope.launch {
                                    doesExistsInDB =
                                        bookMarksVM.deleteDataFromNewsDB(sourceURL = newsBottomSheetContentImpl.sourceURL)
                                    modalBottomSheetState.hide()
                                }.invokeOnCompletion {
                                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL)
                                    if (doesExistsInDB) {
                                        Toast.makeText(
                                            context,
                                            "Bookmark didn't got removed as expected, report it:(",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL)
                                        Toast.makeText(
                                            context,
                                            "Removed from bookmarks:)",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL)
                                }
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                                    false
                                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                                    false
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