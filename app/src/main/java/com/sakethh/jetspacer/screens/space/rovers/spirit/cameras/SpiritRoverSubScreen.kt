package com.sakethh.jetspacer.screens.space.rovers.spirit.cameras

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritCamerasVM
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SpiritRoverSubScreen(cameraName: SpiritCamerasVM.SpiritCameras) {
    val spiritVM: SpiritCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = when (cameraName) {
        SpiritCamerasVM.SpiritCameras.MINITES -> spiritVM.minitesDataFromAPI.value
        SpiritCamerasVM.SpiritCameras.RHAZ -> spiritVM.rhazDataFromAPI.value
        SpiritCamerasVM.SpiritCameras.FHAZ -> spiritVM.fhazDataFromAPI.value
        SpiritCamerasVM.SpiritCameras.PANCAM -> spiritVM.pancamDataFromAPI.value
        SpiritCamerasVM.SpiritCameras.NAVCAM -> spiritVM.navcamDataFromAPI.value
        SpiritCamerasVM.SpiritCameras.RANDOM -> spiritVM.randomCameraDataFromAPI.value
    }
    val isDataLoaded = when (cameraName) {
        SpiritCamerasVM.SpiritCameras.MINITES -> spiritVM.isMinitesDataLoaded.value
        SpiritCamerasVM.SpiritCameras.RHAZ -> spiritVM.isRHAZCamDataLoaded.value
        SpiritCamerasVM.SpiritCameras.FHAZ -> spiritVM.isFHAZDataLoaded.value
        SpiritCamerasVM.SpiritCameras.PANCAM -> spiritVM.isPancamDataLoaded.value
        SpiritCamerasVM.SpiritCameras.NAVCAM -> spiritVM.isNAVCAMDataLoaded.value
        SpiritCamerasVM.SpiritCameras.RANDOM -> spiritVM.isRandomCamerasDataLoaded.value
    }
    val expressionForShowingSnackBar = when (cameraName) {
        SpiritCamerasVM.SpiritCameras.MINITES -> spiritVM._minitesDataFromAPI.value.isEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.RHAZ -> spiritVM._rhazDataFromAPI.value.isEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.FHAZ -> spiritVM._fhazDataFromAPI.value.isEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.PANCAM -> spiritVM._pancamDataFromAPI.value.isEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.NAVCAM -> spiritVM._navcamDataFromAPI.value.isEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.RANDOM -> spiritVM._randomCameraData.value.isEmpty() && isDataLoaded
    }
    val expressionForShowingLoadMoreBtn = when (cameraName) {
        SpiritCamerasVM.SpiritCameras.MINITES -> spiritVM._minitesDataFromAPI.value.isNotEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.RHAZ -> spiritVM._rhazDataFromAPI.value.isNotEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.FHAZ -> spiritVM._fhazDataFromAPI.value.isNotEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.PANCAM -> spiritVM._pancamDataFromAPI.value.isNotEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.NAVCAM -> spiritVM._navcamDataFromAPI.value.isNotEmpty() && isDataLoaded
        SpiritCamerasVM.SpiritCameras.RANDOM -> spiritVM._randomCameraData.value.isNotEmpty() && isDataLoaded
    }
    val currentScreenSolValue = when (cameraName) {
        SpiritCamerasVM.SpiritCameras.MINITES -> SpiritRoverSubScreen.minitesSolValue
        SpiritCamerasVM.SpiritCameras.RHAZ -> SpiritRoverSubScreen.rhazSolValue
        SpiritCamerasVM.SpiritCameras.FHAZ -> SpiritRoverSubScreen.fhazSolValue
        SpiritCamerasVM.SpiritCameras.PANCAM -> SpiritRoverSubScreen.pancamSolValue
        SpiritCamerasVM.SpiritCameras.NAVCAM -> SpiritRoverSubScreen.navcamSolValue
        SpiritCamerasVM.SpiritCameras.RANDOM -> SpiritRoverSubScreen.randomCamSolValue
    }
    LaunchedEffect(key1 = true) {
        when (cameraName) {
            SpiritCamerasVM.SpiritCameras.MINITES -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.MINITES,
                    sol = SpiritRoverSubScreen.minitesSolValue.value.toInt(),
                    page = 0
                )
            }
            SpiritCamerasVM.SpiritCameras.RHAZ -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.RHAZ,
                    sol = SpiritRoverSubScreen.rhazSolValue.value.toInt(),
                    page = 0
                )
            }
            SpiritCamerasVM.SpiritCameras.FHAZ -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
                    sol = SpiritRoverSubScreen.fhazSolValue.value.toInt(),
                    page = 0
                )
            }
            SpiritCamerasVM.SpiritCameras.PANCAM -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.PANCAM,
                    sol = SpiritRoverSubScreen.pancamSolValue.value.toInt(),
                    page = 0
                )
            }
            SpiritCamerasVM.SpiritCameras.NAVCAM -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.NAVCAM,
                    sol = SpiritRoverSubScreen.navcamSolValue.value.toInt(),
                    page = 0
                )
            }
            SpiritCamerasVM.SpiritCameras.RANDOM -> {
                spiritVM.retrieveSpiritCameraData(
                    cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
                    sol = SpiritRoverSubScreen.randomCamSolValue.value.toInt(),
                    page = 0
                )
            }
        }
    }
    fun loadData() {

        SpiritRoverSubScreen.currentPage = 0
        when (cameraName) {
            SpiritCamerasVM.SpiritCameras.MINITES -> {
                spiritVM.isMinitesDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.MINITES)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.MINITES,
                        sol = SpiritRoverSubScreen.minitesSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            SpiritCamerasVM.SpiritCameras.RHAZ -> {
                spiritVM.isRHAZCamDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.RHAZ)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.RHAZ,
                        sol = SpiritRoverSubScreen.rhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            SpiritCamerasVM.SpiritCameras.FHAZ -> {
                spiritVM.isFHAZDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.FHAZ)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
                        sol = SpiritRoverSubScreen.fhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            SpiritCamerasVM.SpiritCameras.PANCAM -> {
                spiritVM.isPancamDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.PANCAM)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.PANCAM,
                        sol = SpiritRoverSubScreen.pancamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            SpiritCamerasVM.SpiritCameras.NAVCAM -> {
                spiritVM.isNAVCAMDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.NAVCAM)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.NAVCAM,
                        sol = SpiritRoverSubScreen.navcamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            SpiritCamerasVM.SpiritCameras.RANDOM -> {
                spiritVM.isRandomCamerasDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.RANDOM)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
                        sol = SpiritRoverSubScreen.randomCamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
        }
    }

    val bottomSheetScaffold = rememberBottomSheetScaffoldState()
    if (solImagesData.isNotEmpty() && expressionForShowingSnackBar && RoversScreenVM.RoverScreenUtils.atLastIndexInLazyVerticalGrid.value) {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.expand()
            if (bottomSheetScaffold.bottomSheetState.isCollapsed) {
                bottomSheetScaffold.bottomSheetState.expand()
            }
        }
    } else {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.collapse()
            if (bottomSheetScaffold.bottomSheetState.isExpanded) {
                bottomSheetScaffold.bottomSheetState.collapse()
            }
        }
    }
    val context = LocalContext.current

    val isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                Toast.makeText(
                    context,
                    "Refreshing data in a moment",
                    Toast.LENGTH_SHORT
                ).show()
                coroutineScope.launch {
                    loadData()
                }
                if (solImagesData.isNotEmpty()) {
                    isRefreshing.value = false
                }
            })
    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
        BottomSheetScaffold(scaffoldState = bottomSheetScaffold,
            sheetGesturesEnabled = false,
            drawerBackgroundColor = Color.Transparent,
            drawerContentColor = Color.Transparent,
            drawerScrimColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            sheetBackgroundColor = Color.Transparent,
            sheetPeekHeight = 0.dp,sheetContent = {
        Snackbar(
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 70.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "You've reached the end, change the sol value to explore more!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                softWrap = true,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            SolTextField(solValue = currentScreenSolValue, onContinueClick = {
                    loadData()
            })
            val statusDescriptionForLoadingScreen =
                if (cameraName != SpiritCamerasVM.SpiritCameras.RANDOM) {
                    "fetching the images from this camera that were captured on sol ${currentScreenSolValue.value}"
                } else {
                    "fetching the images that were captured on sol ${currentScreenSolValue.value}"
                }
            val statusDescriptionFor404Screen =
                if (cameraName != SpiritCamerasVM.SpiritCameras.RANDOM) {
                    "No images were captured by this camera on sol ${currentScreenSolValue.value}. Change the sol value; it may give results"
                } else {
                    "No images were captured on sol ${currentScreenSolValue.value}. Change the sol value; it may give results."
                }
            if (!isDataLoaded) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = statusDescriptionForLoadingScreen,
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = statusDescriptionFor404Screen,
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                    ModifiedLazyVerticalGrid(
                        listData = solImagesData,
                        loadMoreButtonBooleanExpression = expressionForShowingLoadMoreBtn
                    ) {
                        when (cameraName) {
                            SpiritCamerasVM.SpiritCameras.MINITES -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.MINITES,
                                        sol = SpiritRoverSubScreen.minitesSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            SpiritCamerasVM.SpiritCameras.RHAZ -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.RHAZ,
                                        sol = SpiritRoverSubScreen.rhazSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            SpiritCamerasVM.SpiritCameras.FHAZ -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
                                        sol = SpiritRoverSubScreen.fhazSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            SpiritCamerasVM.SpiritCameras.PANCAM -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.PANCAM,
                                        sol = SpiritRoverSubScreen.pancamSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            SpiritCamerasVM.SpiritCameras.NAVCAM -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.NAVCAM,
                                        sol = SpiritRoverSubScreen.navcamSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            SpiritCamerasVM.SpiritCameras.RANDOM -> {
                                coroutineScope.launch {
                                    spiritVM.retrieveSpiritCameraData(
                                        cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
                                        sol = SpiritRoverSubScreen.randomCamSolValue.value.toInt(),
                                        page = SpiritRoverSubScreen.currentPage++
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            scale = true
        )
    }
}

object SpiritRoverSubScreen {
    var randomCamSolValue = mutableStateOf("0")
    var fhazSolValue = mutableStateOf("0")
    var rhazSolValue = mutableStateOf("0")
    var pancamSolValue = mutableStateOf("0")
    var minitesSolValue = mutableStateOf("0")
    var navcamSolValue = mutableStateOf("0")
    var currentPage = 0
}