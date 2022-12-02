package com.sakethh.jetspacer.screens.space.rovers.curiosity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
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
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.chemcam.ChemCamScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.fhaz.FHAZScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mahli.MAHLIScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mardi.MARDIScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mast.MASTScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.navcam.NAVCAMScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.rhaz.RHAZScreen
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
                CuriosityCameras(name = "FHAZ", composable = { FHAZScreen() }),
                CuriosityCameras(name = "RHAZ", composable = { RHAZScreen() }),
                CuriosityCameras(name = "MAST", composable = { MASTScreen() }),
                CuriosityCameras(name = "CHEMCAM", composable = { ChemCamScreen() }),
                CuriosityCameras(name = "MAHLI", composable = { MAHLIScreen() }),
                CuriosityCameras(name = "MARDI", composable = { MARDIScreen() }),
                CuriosityCameras(name = "NAVCAM", composable = { NAVCAMScreen() }),
            )
        }
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            stickyHeader {
                ScrollableTabRow(
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTabIndex = pagerState.currentPage
                ) {
                    curiosityRoverCameras.forEachIndexed { index, camera ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }.start()
                            },
                            modifier = Modifier.padding(
                                end = 20.dp,
                                bottom = 20.dp,
                                start = 20.dp
                            )
                        ) {
                            Text(
                                text = camera.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }
            item {
                HorizontalPager(count = curiosityRoverCameras.size, state = pagerState) {
                    curiosityRoverCameras[pagerState.currentPage].composable()
                }
            }
        }
    }
}

data class CuriosityCameras(val name: String, val composable: @Composable () -> Unit)