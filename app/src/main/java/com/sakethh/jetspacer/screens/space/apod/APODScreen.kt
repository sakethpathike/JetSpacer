package com.sakethh.jetspacer.screens.space.apod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.downloads.DownloadImpl
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.CustomBookMarkData
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.BtmSaveComposableContent
import com.sakethh.jetspacer.screens.constraintSet
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "SimpleDateFormat"
)
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun APODScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val apodScreenVM: APODScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val apodURL = rememberSaveable { mutableStateOf("") }
    val apodHDURL = rememberSaveable { mutableStateOf("") }
    val apodTitle = rememberSaveable { mutableStateOf("") }
    val apodDate = rememberSaveable { mutableStateOf("") }
    val apodDescription = rememberSaveable { mutableStateOf("") }
    val apodMediaType = rememberSaveable { mutableStateOf("") }
    val homeScreenVM: HomeScreenViewModel = viewModel()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val bookMarksVM: BookMarksVM = viewModel()
    val isConnectedToInternet =
        HomeScreenViewModel.Network.connectedToInternet.collectAsState()
    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.SPACE_SCREEN) {
                popUpTo(0)
            }
        }
    }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    AppTheme {
        val isRefreshing = remember { mutableStateOf(false) }
        val pullRefreshState =
            rememberPullRefreshState(refreshing = isRefreshing.value,
                onRefresh = {
                    isRefreshing.value = true
                    coroutineScope.launch {
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
                            apodScreenVM.dataForPagination.value = emptyList()
                            apodScreenVM._dataForAPODPagination.value = emptyList()
                        }
                    }
                }
            )

        Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
            Scaffold(topBar = {
                Column {
                    MediumTopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(
                                text = "APOD Archive",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    Divider(
                        thickness = 0.25.dp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.5f)
                    )
                }

            }) {
                if (apodScreenVM.isDataForAPODPaginationLoaded.value && CurrentHTTPCodes.apodPaginationHTTPCode.value != 200) {
                    Column(
                        modifier = Modifier
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(35.dp))
                        Text(
                            text = "NASA API Usage Limit Exhausted!",
                            fontSize = 45.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            lineHeight = 47.sp,
                            softWrap = true,
                            modifier = Modifier.padding(top =100.dp, start = 25.dp, end = 50.dp)
                        )
                        Text(
                            text = "Change API Key of NASA API from Settings for further data fetching!",
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            lineHeight = 30.sp,
                            softWrap = true,
                            modifier = Modifier.padding(top = 15.dp, start = 25.dp, end = 20.dp)
                        )
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Icon Of \"Error\"",
                            modifier = Modifier
                                .padding(start = 25.dp, top = 22.dp)
                                .size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    var didDataGetAddedInDB = false
                    if (apodScreenVM.isDataForAPODPaginationLoaded.value && apodScreenVM.dataForPagination.value.isNotEmpty()) {
                        ModalBottomSheetLayout(
                            sheetContent = {
                                when (apodScreenVM.currentBtmSheetType.value) {
                                    HomeScreenViewModel.BtmSheetType.Details -> {
                                        APODBottomSheetContent(
                                            inCustomBookmarkScreen = true,
                                            homeScreenViewModel = homeScreenVM,
                                            apodURL = apodURL.value,
                                            apodTitle = apodTitle.value,
                                            apodDate = apodDate.value,
                                            apodDescription = apodDescription.value,
                                            apodMediaType = apodMediaType.value,
                                            onBookMarkClick = {
                                                triggerHapticFeedback(context = context)
                                                bookMarksVM.imgURL = apodURL.value
                                                coroutineScope.launch {
                                                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                                                    val formattedDate = dateFormat.format(Date())
                                                    didDataGetAddedInDB =
                                                        bookMarksVM.addDataToAPODDB(APOD_DB_DTO().apply {
                                                            this.title = apodTitle.value
                                                            this.datePublished = apodDate.value
                                                            this.description = apodDescription.value
                                                            this.imageURL = apodURL.value
                                                            this.mediaType = "image"
                                                            this.isBookMarked = true
                                                            this.category = "APOD"
                                                            this.addedToLocalDBOn = formattedDate
                                                            this.hdImageURL = apodHDURL.value
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
                                                    bookMarksVM.doesThisExistsInAPODIconTxt(
                                                        bookMarksVM.imgURL
                                                    )
                                                }
                                            },
                                            onBookMarkLongPress = {
                                                coroutineScope.launch {
                                                    if (bottomSheetState.isVisible) {
                                                        bottomSheetState.hide()
                                                    }
                                                }.invokeOnCompletion {
                                                    apodScreenVM.currentBtmSheetType.value =
                                                        HomeScreenViewModel.BtmSheetType.BookMarkCollection
                                                    coroutineScope.launch {
                                                        bottomSheetState.show()
                                                    }
                                                }
                                            },
                                            imageHDURL = apodHDURL.value
                                        )
                                    }

                                    HomeScreenViewModel.BtmSheetType.BookMarkCollection -> {
                                        BtmSaveComposableContent(
                                            coroutineScope = coroutineScope,
                                            modalBottomSheetState = bottomSheetState,
                                            data = CustomBookMarkData(
                                                dataType = SavedDataType.APOD,
                                                data = APOD_DB_DTO().apply {
                                                    this.title = apodTitle.value
                                                    this.category = "image"
                                                    this.isBookMarked = true
                                                    this.imageURL = apodURL.value
                                                    this.mediaType = "image"
                                                    this.datePublished = apodDate.value
                                                    this.description = apodDescription.value
                                                    this.hdImageURL = apodHDURL.value
                                                })
                                        )
                                    }
                                }
                            },
                            sheetState = bottomSheetState,
                            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                            sheetBackgroundColor = MaterialTheme.colorScheme.primary
                        ) {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Adaptive(150.dp),
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                                    .padding(it)
                            ) {
                                itemsIndexed(apodScreenVM.dataForPagination.value) { currentAPODItemIndex: Int, apodItem: APOD_DTO ->
                                    if (currentAPODItemIndex == apodScreenVM.dataForPagination.value.lastIndex - 6) {
                                        coroutineScope.launch {
                                            apodScreenVM.fetchAPODData()
                                        }
                                    }
                                    Coil_Image().CoilImage(
                                        imgURL = apodItem.url.toString(),
                                        contentDescription = apodItem.title.toString(),
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .clickable {
                                                apodScreenVM.currentBtmSheetType.value =
                                                    HomeScreenViewModel.BtmSheetType.Details
                                                coroutineScope.launch {
                                                    apodDate.value = apodItem.date.toString()
                                                    apodURL.value = apodItem.url.toString()
                                                    apodDescription.value =
                                                        apodItem.explanation.toString()
                                                    apodTitle.value = apodItem.title.toString()
                                                    apodMediaType.value =
                                                        apodItem.media_type.toString()
                                                    apodHDURL.value = apodItem.hdurl.toString()
                                                    bottomSheetState.show()
                                                }
                                            },
                                        onError = painterResource(id = R.drawable.satellite_filled)
                                    )
                                }
                                item {
                                    androidx.compose.material3.CircularProgressIndicator(
                                        modifier = Modifier.padding(50.dp),
                                        strokeWidth = 4.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(75.dp))
                                }
                            }
                        }
                    } else {
                        Column {
                            Spacer(modifier = Modifier.height(75.dp))
                            StatusScreen(
                                title = "Wait a moment!",
                                description = "fetching the APOD Data from Space!",
                                status = Status.LOADING
                            )
                        }
                    }
                }
            }
            var didDataGetAddedInDB = false
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
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                            false
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value =
                            false
                    }
                )
            }
            PullRefreshIndicator(
                refreshing = isRefreshing.value,
                state = pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                scale = true
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "SimpleDateFormat")
@Composable
fun APODBottomSheetContent(
    homeScreenViewModel: HomeScreenViewModel,
    apodURL: String,
    apodTitle: String,
    apodDate: String,
    apodDescription: String,
    apodMediaType: String,
    onBookMarkClick: () -> Unit,
    inBookMarkScreen: Boolean? = null,
    imageHDURL: String,
    inCustomBookmarkScreen: Boolean = false,
    onBookMarkLongPress: () -> Unit = {},
) {
    val bookMarksVM: BookMarksVM = viewModel()
    bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)

    LazyColumn {
        item {
            ConstraintLayout(constraintSet = constraintSet) {
                APODMediaLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    imageURL = apodURL,
                    apodMediaType = apodMediaType,
                    inAPODBottomSheetContent = true,
                    onBookMarkButtonClick = onBookMarkClick,
                    hdImageURLForAPOD = imageHDURL,
                    onBookmarkLongPress = { onBookMarkLongPress() }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CardForRowGridRaw(
                    title = "Published on",
                    value = apodDate,
                    cardModifier = Modifier
                        .wrapContentHeight()
                        .width(150.dp)
                        .align(CenterVertically)
                )
                var didDataGetAddedInDB = false
                Column(
                    modifier = Modifier.height(85.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    APODBottomSheetChip(
                        onClick = {
                            bookMarksVM.imgURL = apodURL
                            bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)
                            if (inBookMarkScreen == true) {
                                onBookMarkClick()
                            } else {
                                coroutineScope.launch {
                                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
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
                                            this.addedToLocalDBOn = formattedDate
                                            this.hdImageURL = imageHDURL
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
                                    bookMarksVM.doesThisExistsInAPODIconTxt(bookMarksVM.imgURL)
                                }
                            }
                        },
                        imageVector = bookMarksVM.bookMarkIcons.value,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        text = "Bookmark",
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                    APODBottomSheetChip(
                        onClick = {
                            val randomTitle = UUID.randomUUID().toString().substring(0, 6)
                            DownloadImpl(context = context).downloadNewFile(
                                url = imageHDURL,
                                title = "$randomTitle.jpg"
                            )
                            Toast.makeText(
                                context,
                                "Downloading started, check notifications for more information",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        imageVector = Icons.Outlined.FileDownload,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        text = "Download",
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        item {
            Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
            )
        }
        item {
            Text(
                text = "Title : $apodTitle",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )
        }
        item {
            Text(
                text = apodDescription,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                lineHeight = 20.sp,
                textAlign = TextAlign.Start
            )
        }
        /*item {
            Text(
                text = "© $apodCopyright",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                lineHeight = 20.sp,
                textAlign = TextAlign.Start
            )
        }*/

        item {
            Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp,
                    bottom = if (!inCustomBookmarkScreen) 75.dp else 25.dp
                )
            )
        }
    }
    var doesExistsInDB = false
    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
        AlertDialogForDeletingFromDB(
            bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
            onConfirmBtnClick = {
                triggerHapticFeedback(context = context)
                coroutineScope.launch {
                    doesExistsInDB =
                        bookMarksVM.deleteDataFromAPODDB(imageURL = apodURL)
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
                        )
                            .show()
                    }
                    bookMarksVM.doesThisExistsInAPODIconTxt(apodURL)
                }
                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APODBottomSheetChip(
    onClick: () -> Unit, imageVector: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    text: String,
    textColor: Color,
) {
    AssistChip(
        onClick = { onClick() },
        leadingIcon = {
            androidx.compose.material3.Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        label = {
            androidx.compose.material3.Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        shape = RoundedCornerShape(5.dp),
        colors = AssistChipDefaults.assistChipColors(),
        border = AssistChipDefaults.assistChipBorder(borderWidth = 0.dp, borderColor = textColor)
    )
}