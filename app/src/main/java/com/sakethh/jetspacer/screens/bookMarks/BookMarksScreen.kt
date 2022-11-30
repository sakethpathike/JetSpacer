package com.sakethh.jetspacer.screens.bookMarks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.screens.home.APODCardComposable
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
    val bookMarks = bookMarksVM.bookMarks.collectAsState().value

    val apodURL = rememberSaveable { mutableStateOf("") }
    val apodTitle = rememberSaveable { mutableStateOf("") }
    val apodDate = rememberSaveable { mutableStateOf("") }
    val apodDescription = rememberSaveable { mutableStateOf("") }

    AppTheme {
        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "Bookmarks",
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
        }) {

            ModalBottomSheetLayout(
                sheetContent = {
                    APODBottomSheetContent(
                        homeScreenViewModel = homeScreenViewModel,
                        apodURL = apodURL.value,
                        apodTitle = apodTitle.value,
                        apodDate = apodDate.value,
                        apodDescription = apodDescription.value
                    )
                },
                sheetState = bottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                sheetBackgroundColor = MaterialTheme.colorScheme.primary
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    items(bookMarks) { bookMarkedItem ->
                        APODCardComposable(
                            homeScreenViewModel = homeScreenViewModel,
                            apodURL = bookMarkedItem.imageURL,
                            apodDate = bookMarkedItem.datePublished,
                            apodDescription = bookMarkedItem.description,
                            apodTitle = bookMarkedItem.title,
                            inBookMarkScreen = true,
                            imageOnClick = {
                                apodURL.value = bookMarkedItem.imageURL
                                apodTitle.value = bookMarkedItem.title
                                apodDate.value = bookMarkedItem.datePublished
                                apodDescription.value = bookMarkedItem.description
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }
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
}