package com.sakethh.jetspacer.screens.space.rovers.spirit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityCamerasVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun SpiritRoverScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val manifestForCuriosityVM: ManifestForCuriosityVM = viewModel()
    val spiritCamerasVM: SpiritCamerasVM = viewModel()
    LaunchedEffect(true) {
        coroutineScope.launch {
            manifestForCuriosityVM.maxCuriositySol()
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
                        modifier = Modifier.padding(paddingValues = RoversScreenVM.RoverScreenUtils.paddingValues.value),
                    ) {
                        spiritCamerasVM.spiritRoverCameras.forEachIndexed { index, camera ->
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

            HorizontalPager(count = spiritCamerasVM.spiritRoverCameras.size, state = pagerState) { page->
                spiritCamerasVM.spiritRoverCameras[page].composable()
            }
        }

    }
}