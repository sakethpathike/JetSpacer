package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CuriosityCamerasScreen(cameraName: CuriosityCamerasVM.CuriosityCameras) {
    val curiosityCameraVM: CuriosityCamerasVM = viewModel()
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val solImagesData = when (cameraName) {
        CuriosityCamerasVM.CuriosityCameras.CHEMCAM ->
            curiosityCameraVM.chemcamDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.RHAZ -> curiosityCameraVM.rhazDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.FHAZ -> curiosityCameraVM.fhazDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.MAST -> curiosityCameraVM.mastDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.MAHLI -> curiosityCameraVM.mahliDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.MARDI -> curiosityCameraVM.mardiDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.NAVCAM -> curiosityCameraVM.navcamDataFromAPI.value
        CuriosityCamerasVM.CuriosityCameras.RANDOM -> randomCuriosityCameraVM.randomCuriosityCameraData.value
    }
    val expressionForShowingSnackBar = when (cameraName) {
        CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> curiosityCameraVM._chemcamDataFromAPI.value.isEmpty() && curiosityCameraVM.isChemCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RHAZ -> curiosityCameraVM._rhazDataFromAPI.value.isEmpty() && curiosityCameraVM.isRHAZCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.FHAZ -> curiosityCameraVM._fhazDataFromAPI.value.isEmpty() && curiosityCameraVM.isFHAZDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAST -> curiosityCameraVM._mastDataFromAPI.value.isEmpty() && curiosityCameraVM.isMASTCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAHLI -> curiosityCameraVM._mahliDataFromAPI.value.isEmpty() && curiosityCameraVM.isMAHLIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MARDI -> curiosityCameraVM._mardiDataFromAPI.value.isEmpty() && curiosityCameraVM.isMARDIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.NAVCAM -> curiosityCameraVM._navcamDataFromAPI.value.isEmpty() && curiosityCameraVM.isNAVCAMDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RANDOM -> randomCuriosityCameraVM._randomCuriosityCameraData.value.isEmpty() && randomCuriosityCameraVM.isRandomCamerasDataLoaded.value
    }
    val expressionForShowingLoadMoreBtn = when (cameraName) {
        CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> curiosityCameraVM._chemcamDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isChemCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RHAZ -> curiosityCameraVM._rhazDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isRHAZCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.FHAZ -> curiosityCameraVM._fhazDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isFHAZDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAST -> curiosityCameraVM._mastDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isMASTCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAHLI -> curiosityCameraVM._mahliDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isMAHLIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MARDI -> curiosityCameraVM._mardiDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isMARDIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.NAVCAM -> curiosityCameraVM._navcamDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isNAVCAMDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RANDOM -> randomCuriosityCameraVM._randomCuriosityCameraData.value.isNotEmpty() && randomCuriosityCameraVM.isRandomCamerasDataLoaded.value
    }
    val isDataLoaded = when (cameraName) {
        CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> curiosityCameraVM.isChemCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RHAZ -> curiosityCameraVM.isRHAZCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.FHAZ -> curiosityCameraVM.isFHAZDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAST -> curiosityCameraVM.isMASTCamDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MAHLI -> curiosityCameraVM.isMAHLIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.MARDI -> curiosityCameraVM.isMARDIDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.NAVCAM -> curiosityCameraVM.isNAVCAMDataLoaded.value
        CuriosityCamerasVM.CuriosityCameras.RANDOM -> randomCuriosityCameraVM.isRandomCamerasDataLoaded.value
    }
    val currentScreenSolValue = when (cameraName) {
        CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> CuriosityCameraScreen.chemcamSolValue
        CuriosityCamerasVM.CuriosityCameras.RHAZ -> CuriosityCameraScreen.rhazSolValue
        CuriosityCamerasVM.CuriosityCameras.FHAZ -> CuriosityCameraScreen.fhazSolValue
        CuriosityCamerasVM.CuriosityCameras.MAST -> CuriosityCameraScreen.mastSolValue
        CuriosityCamerasVM.CuriosityCameras.MAHLI -> CuriosityCameraScreen.mahliSolValue
        CuriosityCamerasVM.CuriosityCameras.MARDI -> CuriosityCameraScreen.mardiSolValue
        CuriosityCamerasVM.CuriosityCameras.NAVCAM -> CuriosityCameraScreen.navcamSolValue
        CuriosityCamerasVM.CuriosityCameras.RANDOM -> CuriosityCameraScreen.randomCamSolValue
    }
    LaunchedEffect(key1 = true) {
        when (cameraName) {
            CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> {
                curiosityCameraVM.getCHEMCAMData(
                    sol = CuriosityCameraScreen.chemcamSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.RHAZ -> {
                curiosityCameraVM.getRHAZData(
                    sol = CuriosityCameraScreen.rhazSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.FHAZ -> {
                curiosityCameraVM.getFHAZData(
                    sol = CuriosityCameraScreen.fhazSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.MAST -> {
                curiosityCameraVM.getMASTData(
                    sol = CuriosityCameraScreen.mastSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.MAHLI -> {
                curiosityCameraVM.getMAHLIData(
                    sol = CuriosityCameraScreen.mahliSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.MARDI -> {
                curiosityCameraVM.getMARDIData(
                    sol = CuriosityCameraScreen.mardiSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.NAVCAM -> {
                curiosityCameraVM.getNAVCAMData(
                    sol = CuriosityCameraScreen.navcamSolValue.value.toInt(),
                    page = 0
                )
            }
            CuriosityCamerasVM.CuriosityCameras.RANDOM -> {
                randomCuriosityCameraVM.getRandomCuriosityData(
                    sol = CuriosityCameraScreen.randomCamSolValue.value.toInt(),
                    page = 0
                )
            }
        }
    }
    val bottomSheetScaffold = rememberBottomSheetScaffoldState()
    if (solImagesData.isNotEmpty() && expressionForShowingSnackBar && RoversScreenVM.RoverScreenUtils.atLastIndexInLazyVerticalGrid.value) {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.expand()
        }
    } else {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.collapse()
            if (bottomSheetScaffold.bottomSheetState.isExpanded) {
                bottomSheetScaffold.bottomSheetState.collapse()
            }
        }
    }
    fun loadData() {
        when (cameraName) {
            CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> {
                curiosityCameraVM.isChemCamDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.CHEMCAM)
                coroutineScope.launch {
                    curiosityCameraVM.getCHEMCAMData(
                        sol = CuriosityCameraScreen.chemcamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.RHAZ -> {
                curiosityCameraVM.isRHAZCamDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.RHAZ)
                coroutineScope.launch {
                    curiosityCameraVM.getRHAZData(
                        sol = CuriosityCameraScreen.rhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.FHAZ -> {
                curiosityCameraVM.isFHAZDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.FHAZ)
                coroutineScope.launch {
                    curiosityCameraVM.getFHAZData(
                        sol = CuriosityCameraScreen.fhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.MAST -> {
                curiosityCameraVM.isMASTCamDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.MAST)
                coroutineScope.launch {
                    curiosityCameraVM.getMASTData(
                        sol = CuriosityCameraScreen.mastSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.MAHLI -> {
                curiosityCameraVM.isMAHLIDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.MAHLI)
                coroutineScope.launch {
                    curiosityCameraVM.getMAHLIData(
                        sol = CuriosityCameraScreen.mahliSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.MARDI -> {
                curiosityCameraVM.isMARDIDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.MARDI)
                coroutineScope.launch {
                    curiosityCameraVM.getMARDIData(
                        sol = CuriosityCameraScreen.mardiSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.NAVCAM -> {
                curiosityCameraVM.isNAVCAMDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.NAVCAM)
                coroutineScope.launch {
                    curiosityCameraVM.getNAVCAMData(
                        sol = CuriosityCameraScreen.navcamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            CuriosityCamerasVM.CuriosityCameras.RANDOM -> {
                randomCuriosityCameraVM.isRandomCamerasDataLoaded.value = false
                randomCuriosityCameraVM.clearRandomCuriosityCameraData()
                coroutineScope.launch {
                    randomCuriosityCameraVM.getRandomCuriosityData(
                        sol = CuriosityCameraScreen.randomCamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
        }
    }

    val isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                Toast.makeText(context, "Refreshing data in a moment", Toast.LENGTH_SHORT).show()
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
            sheetPeekHeight = 0.dp,
            sheetContent = {
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
                    CuriosityCameraScreen.currentPage = 0
                    loadData()

                })
                val statusDescriptionForLoadingScreen =
                    if (cameraName != CuriosityCamerasVM.CuriosityCameras.RANDOM) {
                        "fetching the images from this camera that were captured on sol ${currentScreenSolValue.value}\n" +
                                "\n" +
                                "if this take toooooo long,\n" +
                                "try changing API Keys from Settings(ಥ _ ಥ)"
                    } else {
                        "fetching the images that were captured on sol ${currentScreenSolValue.value}\n" +
                                "\n" +
                                "if this take toooooo long,\n" +
                                "try changing API Keys from Settings(ಥ _ ಥ)"
                    }
                val statusDescriptionFor404Screen =
                    if (cameraName != CuriosityCamerasVM.CuriosityCameras.RANDOM) {
                        "No images were captured by this camera on sol ${currentScreenSolValue.value}. Change the sol value; it may give results"
                    } else {
                        "No images were captured on sol ${currentScreenSolValue.value}. Change the sol value; it may give results."
                    }

                val isConnectedToInternet =
                    HomeScreenViewModel.Network.connectedToInternet.collectAsState()
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
                } else if (!isDataLoaded && (!isConnectedToInternet.value || !HomeScreenViewModel.Network.isConnectionSucceed.value)) {
                    StatusScreen(
                        title = "",
                        description = statusDescriptionFor404Screen,
                        status = Status.NO_INTERNET
                    )
                } else {
                    ModifiedLazyVerticalGrid(
                        listData = solImagesData,
                        loadMoreButtonBooleanExpression = expressionForShowingLoadMoreBtn
                    ) {
                        coroutineScope.launch {
                            when (cameraName) {
                                CuriosityCamerasVM.CuriosityCameras.CHEMCAM -> {
                                    curiosityCameraVM.getCHEMCAMData(
                                        sol = CuriosityCameraScreen.chemcamSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.RHAZ -> {
                                    curiosityCameraVM.getRHAZData(
                                        sol = CuriosityCameraScreen.rhazSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.FHAZ -> {
                                    curiosityCameraVM.getFHAZData(
                                        sol = CuriosityCameraScreen.fhazSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.MAST -> {
                                    curiosityCameraVM.getMASTData(
                                        sol = CuriosityCameraScreen.mastSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.MAHLI -> {
                                    curiosityCameraVM.getMAHLIData(
                                        sol = CuriosityCameraScreen.mahliSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.MARDI -> {
                                    curiosityCameraVM.getMARDIData(
                                        sol = CuriosityCameraScreen.mardiSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.NAVCAM -> {
                                    curiosityCameraVM.getNAVCAMData(
                                        sol = CuriosityCameraScreen.navcamSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
                                    )
                                }
                                CuriosityCamerasVM.CuriosityCameras.RANDOM -> {
                                    randomCuriosityCameraVM.getRandomCuriosityData(
                                        sol = CuriosityCameraScreen.randomCamSolValue.value.toInt(),
                                        page = CuriosityCameraScreen.currentPage++
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

object CuriosityCameraScreen {
    var randomCamSolValue = mutableStateOf("0")
    var fhazSolValue = mutableStateOf("0")
    var rhazSolValue = mutableStateOf("0")
    var mastSolValue = mutableStateOf("0")
    var chemcamSolValue = mutableStateOf("0")
    var mahliSolValue = mutableStateOf("0")
    var mardiSolValue = mutableStateOf("0")
    var navcamSolValue = mutableStateOf("0")
    var currentPage = 0
}