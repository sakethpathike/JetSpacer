package com.sakethh.jetspacer.screens.space.rovers

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RoverBottomSheetContent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RoversScreen(navController: NavController) {
    val navigationDrawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val roversScreenVM: RoversScreenVM = viewModel()
    val bookMarksVM: BookMarksVM = viewModel()
    val homeScreenViewModel:HomeScreenViewModel= viewModel()
    val context = LocalContext.current
    var didDataGetAddedInDB = false
    BackHandler {
        if (navigationDrawerState.isOpen) {
            coroutineScope.launch {
                navigationDrawerState.close()
            }
        } else if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                roversScreenVM.shouldBtmSheetVisible.value = false
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.SPACE_SCREEN) {
                popUpTo(0)
            }
        }
    }
    if (roversScreenVM.shouldBtmSheetVisible.value) {
        coroutineScope.launch {
            bottomSheetState.show()
        }
    }
    if (!bottomSheetState.isVisible) {
        roversScreenVM.shouldBtmSheetVisible.value = false
    }
    val currentScreenIteration = remember { mutableStateOf(0) }
    val currentScreenName = remember { mutableStateOf("") }

    AppTheme {
        CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl) {
            ModalNavigationDrawer(
                gesturesEnabled = true,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                        Column(
                            Modifier
                                .fillMaxWidth(0.75f)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Select\none of the\nRovers",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                modifier = Modifier.padding(start = 15.dp),
                                maxLines = 3,
                                lineHeight = 41.sp,
                                textAlign = TextAlign.Start
                            )
                            Divider(
                                thickness = 0.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    start = 25.dp,
                                    end = 25.dp,
                                    top = 30.dp,
                                    bottom = 50.dp
                                )
                            )
                            roversScreenVM.listForDrawerContent.forEachIndexed { index, roversScreen ->
                                Row(
                                    modifier = Modifier
                                        .height(75.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            coroutineScope.launch {
                                                navigationDrawerState.close()
                                            }
                                            currentScreenIteration.value = index
                                        }
                                        .wrapContentHeight()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .padding(start = 15.dp)
                                            .height(45.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Camera,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(end = 10.dp)
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .height(45.dp)
                                    ) {
                                        Text(
                                            text = roversScreen.screenName,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 26.sp
                                        )
                                    }
                                }
                            }
                            Divider(
                                thickness = 0.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    start = 25.dp,
                                    end = 25.dp,
                                    top = 50.dp,
                                    bottom = 15.dp
                                )
                            )
                        }
                    }
                },
                drawerState = navigationDrawerState
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                    ModalBottomSheetLayout(
                        sheetContent = {
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
                                            HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value
                                        }
                                        bookMarksVM.doesThisExistsInRoverDBIconTxt(bookMarksVM.imgURL)
                                    }
                                },
                                onConfirmBookMarkDeletionButtonClick = {
                                    coroutineScope.launch {
                                        didDataGetAddedInDB =
                                            bookMarksVM.deleteDataFromMARSDB(imageURL = bookMarksVM.imgURL)
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
                        },
                        sheetState = bottomSheetState,
                        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        sheetBackgroundColor = MaterialTheme.colorScheme.primary
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(
                                            text = currentScreenName.value,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 22.sp,
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    },
                                    actions = {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 15.dp)
                                                .clickable {
                                                    coroutineScope.launch {
                                                        navigationDrawerState.open()
                                                    }
                                                },
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                            }) {
                            RoversScreenVM.RoverScreenUtils.paddingValues.value = it
                            roversScreenVM.listForDrawerContent[currentScreenIteration.value].composable()
                            currentScreenName.value =
                                roversScreenVM.listForDrawerContent[currentScreenIteration.value].screenName
                        }
                    }
                }
            }
        }
    }
}