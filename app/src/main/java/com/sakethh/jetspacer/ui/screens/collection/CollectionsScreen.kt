package com.sakethh.jetspacer.ui.screens.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.CollectionType
import com.sakethh.jetspacer.domain.model.rover_latest_images.Camera
import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import com.sakethh.jetspacer.domain.model.rover_latest_images.Rover
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.apodBtmSheet.APODBtmSheet
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.RoverImageDetailsBtmSheet
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.utils.customMutableRememberSavable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollectionsScreen(navController: NavController) {
    val collectionsScreenViewModel: CollectionsScreenViewModel = viewModel()
    val bookMarkedAPOD =
        collectionsScreenViewModel.bookMarkedAPODData.collectAsStateWithLifecycle()
    val bookMarkedRoverImagesData =
        collectionsScreenViewModel.bookMarkedRoverImagesData.collectAsStateWithLifecycle()
    val scrollableTabData = collectionsScreenViewModel.collectionTabData
    val pagerState = rememberPagerState(pageCount = { scrollableTabData.size })
    val selectedPageName = rememberSaveable {
        mutableStateOf(scrollableTabData.first().name)
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isAPODBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val apodBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isRoverImageBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val roverImageBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedAPODData = customMutableRememberSavable {
        mutableStateOf(
            ModifiedAPODDTO(
                copyright = "",
                date = "",
                explanation = "",
                hdUrl = "",
                mediaType = "",
                title = "",
                url = ""
            )
        )
    }
    val selectedRoverImage = customMutableRememberSavable {
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
    Scaffold(topBar = {
        Column {
            TopAppBar(title = {
                Text(
                    text = "Collection",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp)
                )
            })
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage, modifier = Modifier.fillMaxWidth()
            ) {
                scrollableTabData.forEachIndexed { index, tab ->
                    Tab(selected = tab.name == selectedPageName.value, onClick = {
                        selectedPageName.value = tab.name
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }) {
                        Text(
                            text = tab.name,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
            HorizontalPager(state = pagerState) { page ->
                    when (scrollableTabData[page].type) {
                        CollectionType.APOD_Archive -> {
                            if (bookMarkedAPOD.value.isEmpty()) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Text(
                                        "You haven't bookmarked anything yet.",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 150.dp, start = 15.dp, end = 45.dp)
                                    )
                                }
                                return@HorizontalPager
                            }
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Adaptive(150.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(bookMarkedAPOD.value) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(it.url)
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
                                            .combinedClickable(onClick = {
                                                selectedAPODData.value = ModifiedAPODDTO(
                                                    copyright = it.copyright,
                                                    date = it.date,
                                                    explanation = it.explanation,
                                                    hdUrl = it.hdUrl,
                                                    mediaType = it.mediaType,
                                                    title = it.title,
                                                    url = it.url
                                                )
                                                isAPODBtmSheetVisible.value = true
                                                coroutineScope.launch {
                                                    apodBtmSheetState.show()
                                                }
                                            }, onLongClick = {

                                            }), contentDescription = null
                                    )
                                }
                            }
                        }

                        CollectionType.Mars_Gallery -> {
                            if (bookMarkedRoverImagesData.value.isEmpty()) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Text(
                                        "No items bookmarked yet.",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 150.dp, start = 15.dp, end = 45.dp)
                                    )
                                }
                                return@HorizontalPager
                            }

                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Adaptive(150.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(bookMarkedRoverImagesData.value) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(it.imgUrl)
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
                                            .combinedClickable(onClick = {
                                                selectedRoverImage.value = LatestPhoto(
                                                    camera = Camera(
                                                        fullName = it.cameraFullName,
                                                        id = 0,
                                                        name = "",
                                                        roverID = it.roverId.toInt()
                                                    ),
                                                    earthDate = it.earthDate,
                                                    id = it.id.toInt(),
                                                    imgSrc = it.imgUrl,
                                                    rover = Rover(
                                                        cameras = listOf(),
                                                        id = 0,
                                                        landingDate = "",
                                                        launchDate = "",
                                                        maxDate = "",
                                                        maxSol = 0,
                                                        name = it.roverName,
                                                        status = "",
                                                        totalImages = 0
                                                    ),
                                                    sol = it.sol
                                                )
                                                isRoverImageBtmSheetVisible.value = true
                                                coroutineScope.launch {
                                                    roverImageBtmSheetState.show()
                                                }
                                            }), contentDescription = null
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
    APODBtmSheet(
        modifiedAPODDTO = selectedAPODData.value,
        visible = isAPODBtmSheetVisible,
        btmSheetState = apodBtmSheetState
    )
    RoverImageDetailsBtmSheet(
        image = selectedRoverImage.value,
        visible = isRoverImageBtmSheetVisible,
        btmSheetState = roverImageBtmSheetState
    )
}

