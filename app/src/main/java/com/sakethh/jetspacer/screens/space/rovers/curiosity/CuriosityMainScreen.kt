package com.sakethh.jetspacer.screens.space.rovers.curiosity

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
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM.RoverScreenUtils.paddingValues
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CuriosityRoverScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val manifestForCuriosityVM: ManifestForCuriosityVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    LaunchedEffect(key1 = coroutineScope) {
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

            HorizontalPager(count = roversScreenVM.curiosityRoverCameras.size, state = pagerState) {page->
                roversScreenVM.curiosityRoverCameras[page].composable()
            }
        }

    }
}