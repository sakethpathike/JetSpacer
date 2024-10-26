package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun APODArchiveScreen(navController: NavController) {
    val apodArchiveScreenViewModel: APODArchiveScreenViewModel = viewModel()
    val apodArchiveState = apodArchiveScreenViewModel.apodArchiveState.value
    val context = LocalContext.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()
    val isBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val btmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedAPODData = rememberSaveable(saver = ModifiedAPODDTOSaver) {
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
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(scrollBehavior = topAppBarScrollBehavior, title = {
                Text("APOD Archive", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            })
        }) {
        LazyVerticalStaggeredGrid(
            state = lazyVerticalStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(it)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(Modifier.height(10.dp))
            }
            items(apodArchiveState.data) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it.url)
                        .crossfade(true).build(),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                        )
                        .combinedClickable(onClick = {}, onLongClick = {
                            selectedAPODData.value = it
                            isBtmSheetVisible.value = true
                            coroutineScope.launch {
                                btmSheetState.show()
                            }
                        }), contentDescription = null
                )
            }
        }
    }
    APODBtmSheet(
        modifiedAPODDTO = selectedAPODData.value,
        visible = isBtmSheetVisible,
        btmSheetState = btmSheetState
    )
    LaunchedEffect(lazyVerticalStaggeredGridState.canScrollForward) {
        if (lazyVerticalStaggeredGridState.canScrollForward.not() && apodArchiveState.data.isNotEmpty() && !apodArchiveState.isLoading && !apodArchiveState.error) {
            jetSpacerLog("triggering from launched effect")
            apodArchiveScreenViewModel.retrieveNextBatchOfAPODArchive()
        }
    }
}

private val ModifiedAPODDTOSaver = Saver<MutableState<ModifiedAPODDTO>, String>(save = {
    Json.encodeToString(it.value)
}, restore = {
    mutableStateOf(Json.decodeFromString<ModifiedAPODDTO>(it))
})