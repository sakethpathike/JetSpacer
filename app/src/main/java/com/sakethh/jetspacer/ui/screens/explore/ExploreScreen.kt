package com.sakethh.jetspacer.ui.screens.explore

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController

import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sakethh.jetspacer.core.common.Network
import com.sakethh.jetspacer.data.repository.ISSInfoRepoImpl
import com.sakethh.jetspacer.data.repository.NasaRepositoryImpl
import com.sakethh.jetspacer.domain.useCase.FetchISSLocationUseCase
import com.sakethh.jetspacer.domain.useCase.FetchImagesFromNasaImageLibraryUseCase
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.components.InfoCard
import com.sakethh.jetspacer.ui.components.pulsateEffect
import com.sakethh.jetspacer.ui.navigation.HyleNavigation
import com.sakethh.jetspacer.ui.screens.explore.search.SearchResultComponent
import com.sakethh.jetspacer.ui.screens.home.HeadlineDetailComponent
import com.sakethh.jetspacer.ui.screens.home.HomeScreenViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExploreScreen() {
    val navController: NavController = LocalNavController.current
    val localContext = LocalContext.current
    val exploreScreenViewModel: ExploreScreenViewModel = viewModel(factory = viewModelFactory {
        initializer {
            ExploreScreenViewModel(
                navController = navController,
                context = localContext,
                fetchISSLocationUseCase = FetchISSLocationUseCase(
                    ISSInfoRepoImpl(Network.ktorClient)
                ),
                fetchImagesFromNasaImageLibraryUseCase = FetchImagesFromNasaImageLibraryUseCase(
                    NasaRepositoryImpl(Network.ktorClient)
                ),
            )
        }
    })
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
                        })
                }
            },
            expanded = ExploreScreenViewModel.isSearchBarExpanded.value,
            onExpandedChange = {
                ExploreScreenViewModel.isSearchBarExpanded.value = it
            }) {
            if (searchResultsState.value.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularWavyProgressIndicator()
                }
            }
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(5.dp), columns = StaggeredGridCells.Adaptive(150.dp)
            ) {
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
                                    modifier = Modifier.padding(end = 50.dp)
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
                                    modifier = Modifier.padding(end = 50.dp)
                                )
                            }
                        }
                    }
                }
                items(searchResultsState.value.data) {
                    SearchResultComponent(
                        nasaImageLibrarySearchDTOFlatten = it.first,
                        palette = it.second,
                        onItemClick = {
                            navController.navigate(
                                HyleNavigation.Explore.SearchResultScreen(
                                    Json.encodeToString(it)
                                )
                            )
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
                text = "ISS",
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
                text = "Current Location of the International Space Station",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
            Spacer(Modifier.height(5.dp))
            AnimatedContent(
                targetState = issLocationState.value.timestamp.isBlank() to issLocationState.value.error,
                contentKey = {
                    it
                }) {

                if (it.second) {
                    InfoCard(
                        modifier = Modifier.padding(start = 15.dp, top = 5.dp, end = 15.dp),
                        info = issLocationState.value.errorMessage
                    )
                    return@AnimatedContent
                }

                if (it.first) {
                    LinearWavyProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 10.dp)
                    )
                    return@AnimatedContent
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    HeadlineDetailComponent(
                        string = issLocationState.value.timestamp, Icons.Default.Event
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
                    InfoCard(
                        info = "Data will be refreshed after every 5 seconds",
                        modifier = Modifier.padding(top = 15.dp)
                    )
                }
            }
            HorizontalDivider(Modifier.padding(15.dp))
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                ExploreSectionItem(
                    palette = exploreScreenViewModel.apodArchiveBannerColors,
                    imgURL = HomeScreenViewModel.currentAPODImgURL.ifBlank { "https://apod.nasa.gov/apod/image/2410/IC63_1024.jpg" },
                    itemTitle = "APOD Archive",
                    onClick = {
                        navController.navigate(HyleNavigation.Explore.APODArchiveScreen)
                    })
                Spacer(Modifier.height(10.dp))
                ExploreSectionItem(
                    palette = exploreScreenViewModel.marsGalleryBannerColors,
                    imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg/435px-Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg",
                    itemTitle = "Mars Gallery",
                    onClick = {
                        navController.navigate(HyleNavigation.Explore.MarsGalleryScreen)
                    })
                Spacer(Modifier.height(110.dp))
            }
        }
    }
}

@Composable
private fun ExploreSectionItem(
    palette: List<Color>, imgURL: String, itemTitle: String, onClick: () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    Box(
        Modifier
            .pulsateEffect()
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(
                color = MaterialTheme.colorScheme.primaryContainer,
                width = 2.5.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable { onClick() }) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(imgURL).crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = if (palette.size >= 2) palette else listOf(
                                colorScheme.surface, Color.Transparent
                            )
                        ), blendMode = BlendMode.Multiply
                    )
                }
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = itemTitle,
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