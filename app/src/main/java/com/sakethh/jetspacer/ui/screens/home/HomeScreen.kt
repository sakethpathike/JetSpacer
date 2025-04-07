package com.sakethh.jetspacer.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.apodBtmSheet.APODBtmSheet
import com.sakethh.jetspacer.ui.screens.headlines.HeadlineDetailComponent
import com.sakethh.jetspacer.ui.navigation.JetSpacerNavigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val apodDataState = homeScreenViewModel.apodState
    val epicDataState = homeScreenViewModel.epicState
    val isAPODBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val apodBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
            .animateContentSize()
    ) {
        item {
            Spacer(Modifier.windowInsetsPadding(WindowInsets.statusBars))
            Spacer(Modifier.height(15.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                FilledTonalIconButton(onClick = {
                    navController.navigate(JetSpacerNavigation.Latest.Settings)
                }) {
                    Icon(Icons.Default.Settings, null)
                }
            }
            Text(
                "APOD",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                    .padding(5.dp)
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Astronomy Picture of the Day",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(15.dp))
        }

        item {
            if (apodDataState.value.isLoading || apodDataState.value.error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (apodDataState.value.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "${apodDataState.value.statusCode}\n${apodDataState.value.statusDescription}",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(apodDataState.value.apod.url.trim())
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            isAPODBtmSheetVisible.value = true
                            coroutineScope.launch {
                                apodBtmSheetState.show()
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (apodDataState.value.apod.date.trim().isNotBlank()) {
            item {
                Spacer(Modifier.height(15.dp))
                HeadlineDetailComponent(
                    string = apodDataState.value.apod.date.trim(),
                    imageVector = Icons.Outlined.CalendarToday,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
        }
        if (apodDataState.value.apod.copyright.trim().isNotBlank()) {
            item {
                Spacer(Modifier.height(5.dp))
                HeadlineDetailComponent(
                    string = apodDataState.value.apod.copyright.trim().replace("\n", ""),
                    imageVector = Icons.Outlined.Copyright,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
        }
        if (apodDataState.value.apod.title.trim().isNotBlank()) {
            item {
                Spacer(Modifier.height(15.dp))
                Text(
                    "Title",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    apodDataState.value.apod.title.trim(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (apodDataState.value.apod.explanation.trim().isNotBlank()) {
            item {
                val isExplanationExpanded = rememberSaveable {
                    mutableStateOf(false)
                }
                Spacer(Modifier.height(15.dp))
                Text(
                    "Explanation",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(2.dp))
                Box {
                    Text(
                        apodDataState.value.apod.explanation.trim(),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isExplanationExpanded.value) Int.MAX_VALUE else 3,
                        modifier = Modifier.clickable {
                            isExplanationExpanded.value = !isExplanationExpanded.value
                        })
                }
            }
        }
        item {
            Spacer(Modifier.height(15.dp))
            HorizontalDivider()
        }
        item {
            Spacer(Modifier.height(15.dp))
            Text(
                "EPIC",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                    .padding(5.dp)
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Earth Polychromatic Imaging Camera",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            if (epicDataState.value.data.isNotEmpty()) {
                Spacer(Modifier.height(5.dp))
                HeadlineDetailComponent(
                    string = epicDataState.value.data.first().date,
                    imageVector = Icons.Outlined.CalendarToday,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
            Spacer(Modifier.height(15.dp))
        }
        item {
            if (epicDataState.value.isLoading || epicDataState.value.error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (epicDataState.value.error) {
                        Text(
                            text = "${epicDataState.value.statusCode}\n${epicDataState.value.statusDescription}",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        if (epicDataState.value.data.isNotEmpty()) {
            itemsIndexed(epicDataState.value.data) { index, epicItem ->
                AsyncImage(
                    model = ImageRequest.Builder(context).data(epicItem.imageURL).crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                        )
                        .clickable {

                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(5.dp))
                HeadlineDetailComponent(
                    string = epicItem.timeWhenImageWasCaptured,
                    imageVector = Icons.Outlined.AccessTime,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )/*Spacer(Modifier.height(5.dp))
                HeadlineDetailComponent(
                    string = epicItem.distanceBetweenSunAndEarth.toString(),
                    imageVector = Icons.Outlined.NearMe,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )*/
                if (index != epicDataState.value.data.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 5.dp)
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(150.dp))
        }
    }
    APODBtmSheet(
        modifiedAPODDTO = apodDataState.value.apod,
        visible = isAPODBtmSheetVisible,
        btmSheetState = apodBtmSheetState
    )
}