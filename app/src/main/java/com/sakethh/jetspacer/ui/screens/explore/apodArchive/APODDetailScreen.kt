package com.sakethh.jetspacer.ui.screens.explore.apodArchive

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.ui.screens.headlines.HeadlineDetailComponent
import com.sakethh.jetspacer.ui.screens.home.components.ImageActionsRow
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.utils.rememberSerializableObject
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.APODDetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope, apod: ModifiedAPODDTO
) {

    val commonModifier = remember {
        Modifier.padding(start = 15.dp, end = 15.dp)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        repeat(2) {
            Spacer(
                Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(context).data(apod.url).crossfade(true).build(),
            contentDescription = null,
            modifier = commonModifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "APOD${apod.url}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .clip(RoundedCornerShape(15.dp))
                .border(
                    1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                ),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(15.dp))
        Box(modifier = commonModifier) {
            HeadlineDetailComponent(
                string = apod.date.trim(),
                imageVector = Icons.Outlined.CalendarToday,
                fontSize = 14.sp,
                iconSize = 20.dp
            )
        }

        Spacer(Modifier.height(5.dp))
        val copyright = rememberSaveable {
            apod.copyright.trim().replace("\n", "")
        }
        if (copyright.isNotBlank()) {
            Box(commonModifier) {
                HeadlineDetailComponent(
                    string = copyright,
                    imageVector = Icons.Outlined.Copyright,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
        }
        ImageActionsRow(
            openInBrowserURL = "https://apod.nasa.gov",
            supportsBothHDDAndSD = true,
            hdURL = apod.hdUrl,
            sdURL = apod.url,
            sdDownloadDesc = "APOD: Downloading SD image (${apod.date})",
            hdDownloadDesc = "APOD: Downloading HD image (${apod.date})"
        )
        Spacer(Modifier.height(15.dp))
        Text(
            modifier = commonModifier,
            text = "Title",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )

        SelectionContainer {
            Text(
                text = apod.title.trim(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = commonModifier
            )
        }

        Spacer(Modifier.height(15.dp))
        Text(
            modifier = commonModifier,
            text = "Explanation",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(2.dp))
        SelectionContainer {
            Text(
                text = apod.explanation.trim(),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                modifier = commonModifier
            )
        }
        Spacer(
            Modifier.navigationBarsPadding()
        )
    }
}