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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
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
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CuriosityRoverScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val curiosityRoverCameras =
        rememberSaveable {
            listOf(
                CuriosityCameras(name = "Random", composable = { RandomCuriosityCameraScreen() }),
                CuriosityCameras(name = "FHAZ", composable = { FHAZCuriosityCameraScreen() }),
                CuriosityCameras(name = "RHAZ", composable = { RHAZCuriosityCameraScreen() }),
                CuriosityCameras(name = "MAST", composable = { MASTCuriosityCameraScreen() }),
                CuriosityCameras(name = "CHEMCAM", composable = { ChemCamCuriosityCameraScreen() }),
                CuriosityCameras(name = "MAHLI", composable = { MAHLICuriosityCameraScreen() }),
                CuriosityCameras(name = "MARDI", composable = { MARDICuriosityCameraScreen() }),
                CuriosityCameras(name = "NAVCAM", composable = { NAVCAMCuriosityCameraScreen() }),
            )
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
                        curiosityRoverCameras.forEachIndexed { index, camera ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {

                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }.start()
                                },
                                modifier = Modifier.padding(15.dp )
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
                    SolTextField()
                }
            }

        HorizontalPager(count = curiosityRoverCameras.size, state = pagerState) {
            curiosityRoverCameras[pagerState.currentPage].composable()
        }}

    }
}

data class CuriosityCameras(val name: String, val composable: @Composable () -> Unit)