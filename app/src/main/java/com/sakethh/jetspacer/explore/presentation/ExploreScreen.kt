package com.sakethh.jetspacer.explore.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.common.presentation.navigation.APODArchiveScreen
import com.sakethh.jetspacer.common.presentation.navigation.MarsGalleryRoute
import com.sakethh.jetspacer.common.presentation.navigation.SearchResultScreenRoute
import com.sakethh.jetspacer.news.presentation.HeadlineDetailComponent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
    val exploreScreenViewModel: ExploreScreenViewModel = viewModel()
    val searchResultsState = exploreScreenViewModel.searchResultsState
    val issLocationState = exploreScreenViewModel.issLocationState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (ExploreScreenViewModel.isSearchBarExpanded.value) 0.dp else 15.dp),
            inputField = {
                ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                    SearchBarDefaults.InputField(
                        placeholder = {
                            Text(
                                "Search in NASA Image Library",
                                modifier = Modifier.basicMarquee(),
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        trailingIcon = {
                            if (ExploreScreenViewModel.isSearchBarExpanded.value) {
                                IconButton(onClick = {
                                    exploreScreenViewModel.querySearch.value = ""
                                    ExploreScreenViewModel.isSearchBarExpanded.value = false
                                }) {
                                    Icon(Icons.Filled.Clear, null)
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, null)
                        },
                        query = exploreScreenViewModel.querySearch.value,
                        onQueryChange = {
                            exploreScreenViewModel.querySearch.value = it
                        },
                        onSearch = {

                        },
                        expanded = ExploreScreenViewModel.isSearchBarExpanded.value,
                        onExpandedChange = {
                            ExploreScreenViewModel.isSearchBarExpanded.value = it
                        }
                    )
                }
            },
            expanded = ExploreScreenViewModel.isSearchBarExpanded.value,
            onExpandedChange = {
                ExploreScreenViewModel.isSearchBarExpanded.value = it
            }) {
            if (searchResultsState.value.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(150.dp)) {
                if (searchResultsState.value.error) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier.padding(top = 75.dp, start = 15.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text(
                                    text = "${searchResultsState.value.statusCode}\n${searchResultsState.value.statusDescription}",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 32.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(end = 50.dp)
                                )
                            }
                        }
                    }
                    return@LazyVerticalStaggeredGrid
                }
                if (searchResultsState.value.data.isEmpty() && !searchResultsState.value.isLoading && exploreScreenViewModel.querySearch.value.isNotBlank()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier.padding(top = 75.dp, start = 15.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text(
                                    text = "Nothing found here",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 32.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(end = 50.dp)
                                )
                            }
                        }
                    }
                }
                items(searchResultsState.value.data) {
                    SearchResultComponent(it, onItemClick = {
                        navController.navigate(SearchResultScreenRoute(Json.encodeToString(it)))
                    })
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(Modifier.height(25.dp))
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
        Text(
            "ISS",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 15.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                .padding(5.dp)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "Current Location of the International Space Station",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
        )
        Spacer(Modifier.height(5.dp))
            if (issLocationState.value.timestamp.isBlank()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 10.dp)
                )
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    HeadlineDetailComponent(
                        string = issLocationState.value.timestamp,
                        Icons.Default.Event
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                                .padding(10.dp)
                        ) {
                            Text(
                                "Latitude",
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                issLocationState.value.latitude,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.width(5.dp))
                        Column(
                            Modifier
                                .padding(end = 15.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                                .padding(10.dp)
                        ) {
                            Text(
                                "Longitude",
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                issLocationState.value.longitude,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Card(
                        border = BorderStroke(
                            1.dp,
                            contentColorFor(MaterialTheme.colorScheme.surface)
                        ),
                        colors = CardDefaults.cardColors(containerColor = AlertDialogDefaults.containerColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(
                                    top = 10.dp, bottom = 10.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(
                                            start = 10.dp, end = 10.dp
                                        )
                                )
                            }
                            Text(
                                text = "Data will be refreshed after every 5 seconds",
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                            )
                        }
                    }
                }
            }
        HorizontalDivider(Modifier.padding(15.dp))
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            ExploreSectionItem(
                imgURL = "https://apod.nasa.gov/apod/image/2410/IC63_1024.jpg",
                itemTitle = "APOD Archive",
                onClick = {
                    navController.navigate(APODArchiveScreen)
                }
            )
            Spacer(Modifier.height(10.dp))
            ExploreSectionItem(
                imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg/435px-Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg",
                itemTitle = "Mars Gallery",
                onClick = {
                    navController.navigate(MarsGalleryRoute)
                }
            )
            Spacer(Modifier.height(110.dp))
        }
        }
    }
}

@Composable
private fun ExploreSectionItem(imgURL: String, itemTitle: String, onClick: () -> Unit) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    Box(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.25f))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imgURL)
                .crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            listOf(
                                colorScheme.surface, Color.Transparent
                            )
                        ), blendMode = BlendMode.DstIn
                    )
                }
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        Text(
            itemTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 15.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(5.dp)
                .align(Alignment.BottomStart)
        )
    }
}