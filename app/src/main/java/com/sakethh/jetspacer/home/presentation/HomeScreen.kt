package com.sakethh.jetspacer.home.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.news.presentation.HeadlineDetailComponent

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val apodDataState = homeScreenViewModel.apodState
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
            .animateContentSize()
    ) {
        item {
            Spacer(Modifier.height(50.dp))
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
            if (apodDataState.value.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(apodDataState.value.apod.url)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp,
                            LocalContentColor.current.copy(0.25f),
                            RoundedCornerShape(15.dp)
                        )
                        .clickable {

                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (apodDataState.value.apod.date.isNotBlank()) {
            item {
                Spacer(Modifier.height(15.dp))
                HeadlineDetailComponent(
                    string = apodDataState.value.apod.date,
                    imageVector = Icons.Outlined.CalendarToday,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
        }
        if (apodDataState.value.apod.copyright.isNotBlank()) {
            item {
                Spacer(Modifier.height(5.dp))
                HeadlineDetailComponent(
                    string = apodDataState.value.apod.copyright,
                    imageVector = Icons.Outlined.Copyright,
                    fontSize = 14.sp,
                    iconSize = 20.dp
                )
            }
        }
        if (apodDataState.value.apod.title.isNotBlank()) {
            item {
                Spacer(Modifier.height(15.dp))
                Text(
                    "Title",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    apodDataState.value.apod.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (apodDataState.value.apod.explanation.isNotBlank()) {
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
                        apodDataState.value.apod.explanation,
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isExplanationExpanded.value) Int.MAX_VALUE else 3,
                        modifier = Modifier.clickable {
                            isExplanationExpanded.value = !isExplanationExpanded.value
                        }
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(150.dp))
        }
    }
}