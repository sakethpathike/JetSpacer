package com.sakethh.jetspacer.explore.marsGallery.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.sakethh.jetspacer.common.presentation.components.LabelValueCard
import com.sakethh.jetspacer.common.presentation.utils.customMutableRememberSavable
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.Camera
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.LatestPhoto
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.Rover
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class, ExperimentalLayoutApi::class
)
@Composable
fun MarsGalleryScreen(navController: NavController) {
    val marsGalleryScreenViewModel: MarsGalleryScreenViewModel = viewModel()
    val latestImagesState = marsGalleryScreenViewModel.latestImagesState
    val context = LocalContext.current
    val isRoverImageDetailsBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val roverImageDetailsBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedLatestImage = customMutableRememberSavable {
        mutableStateOf(
            LatestPhoto(
                camera = Camera(
                    fullName = "",
                    id = 0,
                    name = "",
                    roverID = 0
                ), earthDate = "", id = 0, imgSrc = "", rover = Rover(
                    cameras = listOf(),
                    id = 0,
                    landingDate = "",
                    launchDate = "",
                    maxDate = "",
                    maxSol = 0,
                    name = "",
                    status = "",
                    totalImages = 0
                ), sol = 0
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val isInfoForRoverBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val btmSheetStateForRoverInfo = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isDropDownForRoverSelectionExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Column {
                Text("Mars Gallery", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = latestImagesState.value.roverName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
            }
        }, navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }, actions = {
            IconButton(onClick = {
                isInfoForRoverBtmSheetVisible.value = true
            }) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                isDropDownForRoverSelectionExpanded.value =
                    !isDropDownForRoverSelectionExpanded.value
            }) {
                Icon(
                    if (isDropDownForRoverSelectionExpanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxWidth(), columns = StaggeredGridCells.Adaptive(150.dp)
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = "Latest",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(15.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (latestImagesState.value.isLoading) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                items(latestImagesState.value.data.latestImages) { latestImage ->
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(latestImage.imgSrc)
                            .crossfade(true).build(),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(5.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                1.5.dp,
                                LocalContentColor.current.copy(0.25f),
                                RoundedCornerShape(15.dp)
                            )
                            .clickable {
                                selectedLatestImage.value = latestImage
                                isRoverImageDetailsBtmSheetVisible.value = true
                                coroutineScope.launch {
                                    roverImageDetailsBtmSheetState.show()
                                }
                            },
                        contentDescription = null
                    )
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    if (latestImagesState.value.isLoading.not() && latestImagesState.value.error.not()) {
                        Column {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            )
                            Text(
                                "That's all the data found.",
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(200.dp))
                }
            }
            FilledTonalIconButton(
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 15.dp, bottom = 25.dp)
                    .size(45.dp)
            ) {
                Icon(Icons.Default.ChevronRight, null)
            }
            if (isDropDownForRoverSelectionExpanded.value) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 5.dp)
                        .clip(
                            MenuDefaults.shape
                        )
                        .border(
                            width = 1.dp,
                            color = MenuDefaults.itemColors().textColor.copy(0.25f),
                            shape = MenuDefaults.shape
                        )
                        .background(MenuDefaults.containerColor)
                ) {
                    Text(
                        text = "Switch to",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                        color = MenuDefaults.itemColors().textColor,
                        fontSize = 12.sp
                    )
                    com.sakethh.jetspacer.explore.marsGallery.presentation.utils.Rover.entries.forEach { rover ->
                        if (rover.name == latestImagesState.value.roverName) {
                            return@forEach
                        }
                        Box(
                            Modifier
                                .width(150.dp)
                                .clickable {
                                    isDropDownForRoverSelectionExpanded.value =
                                        !isDropDownForRoverSelectionExpanded.value
                                    marsGalleryScreenViewModel.loadLatestImagesFromRover(rover.name.lowercase())
                                }) {
                            Text(
                                text = rover.name,
                                modifier = Modifier.padding(15.dp),
                                color = MenuDefaults.itemColors().textColor,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
    RoverImageDetailsBtmSheet(
        image = selectedLatestImage.value,
        visible = isRoverImageDetailsBtmSheetVisible,
        btmSheetState = roverImageDetailsBtmSheetState
    )
    if (isInfoForRoverBtmSheetVisible.value) {
        ModalBottomSheet(onDismissRequest = {
            coroutineScope.launch {
                btmSheetStateForRoverInfo.hide()
            }.invokeOnCompletion {
                isInfoForRoverBtmSheetVisible.value = false
            }
        }, sheetState = btmSheetStateForRoverInfo) {
            if (latestImagesState.value.data.latestImages.isEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                return@ModalBottomSheet
            }
            LabelValueCard(
                title = "",
                value = latestImagesState.value.data.latestImages.first().rover.name
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            Row {
                LabelValueCard(
                    title = "Launch Date",
                    value = latestImagesState.value.data.latestImages.first().rover.launchDate,
                    outerPaddingValues = PaddingValues(start = 15.dp)
                )
                LabelValueCard(
                    title = "Landing Date",
                    value = latestImagesState.value.data.latestImages.first().rover.landingDate
                )
            }
            Row(Modifier.padding(top = 15.dp)) {
                LabelValueCard(
                    title = "Max Sol",
                    value = latestImagesState.value.data.latestImages.first().rover.maxSol.toString(),
                    outerPaddingValues = PaddingValues(start = 15.dp)
                )
                LabelValueCard(
                    title = "Max Date",
                    value = latestImagesState.value.data.latestImages.first().rover.maxDate
                )
            }
            Spacer(Modifier.height(15.dp))
            LabelValueCard(
                title = "Status",
                value = latestImagesState.value.data.latestImages.first().rover.status.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            )
            Spacer(Modifier.height(15.dp))
            LabelValueCard(
                title = "Cameras",
                value = latestImagesState.value.data.latestImages.first().rover.cameras.joinToString(
                    separator = "\n"
                ) {
                    "• ${it.fullName}"
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}