package com.sakethh.jetspacer.ui.screens.explore.marsGallery

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import com.sakethh.jetspacer.ui.components.LabelValueCard
import com.sakethh.jetspacer.ui.screens.home.components.ImageActionsRow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RoverImageDetailsScreen(
    image: LatestPhoto, animatedVisibilityScope: AnimatedVisibilityScope
) {
    val coroutineScope = rememberCoroutineScope()
    val isBtmColumnExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val localUriHandler = LocalUriHandler.current
    val localClipboardManager = LocalClipboardManager.current
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 15.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(image.imgSrc).crossfade(true).build(),
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "MARS${image.imgSrc}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                    ),
                contentDescription = null
            )
            ImageActionsRow(
                openInBrowserURL = image.imgSrc,
                supportsBothHDDAndSD = false,
                hdURL = null,
                sdURL = image.imgSrc,
                sdDownloadDesc = "Mars Gallery: Downloading image (${image.rover.name}, ${image.camera.name}, ${image.sol})",
                hdDownloadDesc = null,
                paddingValues = PaddingValues(top = 10.dp)
            )
            Row(Modifier.padding(top = 10.dp).horizontalScroll(rememberScrollState())) {
                LabelValueCard(
                    title = "Sol",
                    value = image.sol.toString(),
                    outerPaddingValues = PaddingValues(end = 5.dp)
                )
                LabelValueCard(
                    title = "Earth Date",
                    value = image.earthDate,
                    outerPaddingValues = PaddingValues(end = 5.dp)
                )
                LabelValueCard(
                    title = "Captured by",
                    value = image.camera.fullName,
                    outerPaddingValues = PaddingValues()
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                image.rover.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}