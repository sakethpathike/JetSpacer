package com.sakethh.jetspacer.screens.space.rovers.curiosity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM.RoverScreenUtils.paddingValues
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.chemcam.ChemCamCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.fhaz.FHAZCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mahli.MAHLICuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mardi.MARDICuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mast.MASTCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.navcam.NAVCAMCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.rhaz.RHAZCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CuriosityRoverScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val manifestVM: ManifestVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    LaunchedEffect(key1 = coroutineScope) {
        coroutineScope.launch {
            manifestVM.maxCuriositySol()
        }
    }

    AppTheme {
        Column {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                stickyHeader {
                    ScrollableTabRow(
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.padding(paddingValues = paddingValues.value),
                    ) {
                        roversScreenVM.curiosityRoverCameras.forEachIndexed { index, camera ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {

                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }.start()
                                },
                                modifier = Modifier.padding(15.dp)
                            ) {
                                Text(
                                    text = camera.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            HorizontalPager(count = roversScreenVM.curiosityRoverCameras.size, state = pagerState) {
                roversScreenVM.curiosityRoverCameras[pagerState.currentPage].composable()
            }
        }

    }
}
