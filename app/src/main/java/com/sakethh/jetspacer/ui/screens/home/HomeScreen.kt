package com.sakethh.jetspacer.ui.screens.home

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sakethh.jetspacer.common.utils.logger
import com.sakethh.jetspacer.domain.model.article.Article
import com.sakethh.jetspacer.domain.model.article.Source
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.components.Label
import com.sakethh.jetspacer.ui.components.LabelValueCard
import com.sakethh.jetspacer.ui.components.pulsateEffect
import com.sakethh.jetspacer.ui.navigation.HyleNavigation
import com.sakethh.jetspacer.ui.screens.headlines.HeadlineDetailComponent
import com.sakethh.jetspacer.ui.screens.headlines.components.TopHeadlineComponent
import com.sakethh.jetspacer.ui.screens.home.components.ImageActionsRow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.HomeScreen(animatedVisibilityScope: AnimatedVisibilityScope) {
    val navController: NavController = LocalNavController.current
    val context = LocalContext.current
    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(context)
        }
    })
    val apodDataState = homeScreenViewModel.apodState.value
    val epicDataState = homeScreenViewModel.epicState
    val isAPODBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val apodBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val horizontalPager = rememberPagerState {
        epicDataState.data.size
    }
    val commonModifier = remember {
        Modifier.padding(start = 15.dp, end = 15.dp)
    }
    var currentEPICCapturedTime by rememberSaveable {
        mutableStateOf("")
    }
    var currentDistanceToEarthFromTheEPIC by rememberSaveable {
        mutableStateOf("")
    }
    var currentDistanceToSunFromEPIC by rememberSaveable {
        mutableStateOf("")
    }
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current

    val sliderValue = rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val topHeadlinesState by homeScreenViewModel.topHeadLinesState
    val colorScheme = MaterialTheme.colorScheme
    val lazyColumnState = rememberLazyListState()
    val localDensity = LocalDensity.current
    val apodImageURL = apodDataState.apod.first.url.trim()
    var apodDownloadBtnClicked by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(epicDataState.data, horizontalPager.currentPage) {
        currentEPICCapturedTime = try {
            epicDataState.data[horizontalPager.currentPage].timeWhenImageWasCaptured
        } catch (_: Exception) {
            ""
        }
        currentDistanceToSunFromEPIC = try {
            epicDataState.data[horizontalPager.currentPage].distanceToSunFromEPIC.toString()
        } catch (_: Exception) {
            ""
        }
        currentDistanceToEarthFromTheEPIC = try {
            epicDataState.data[horizontalPager.currentPage].distanceToEarthFromTheEPIC.toString()
        } catch (_: Exception) {
            ""
        }
    }
    Box {
        Box(
            Modifier
                .background(
                    Brush.radialGradient(center = remember {
                        Offset(0f, 0f)
                    }, colors = remember {
                        listOf(
                            colorScheme.primary,
                            Color.Transparent,
                        ).map {
                            it.copy(0.125f)
                        }
                    })
                )
                .zIndex(-1f)
                .fillMaxSize()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(), state = lazyColumnState
        ) {
            item {
                Spacer(Modifier.windowInsetsPadding(WindowInsets.statusBars))
            }
            item {
                Spacer(Modifier.height(30.dp))
                Label(text = "EPIC", modifier = commonModifier)
                Spacer(Modifier.height(5.dp))
                Text(
                    modifier = commonModifier,
                    text = "Earth Polychromatic Imaging Camera",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (epicDataState.data.isNotEmpty()) {
                    Spacer(Modifier.height(5.dp))
                    Box(modifier = commonModifier) {
                        HeadlineDetailComponent(
                            string = epicDataState.data.first().date,
                            imageVector = Icons.Outlined.CalendarToday,
                            fontSize = 14.sp,
                            iconSize = 20.dp
                        )
                    }
                }
                Spacer(Modifier.height(15.dp))
            }
            item {
                if (epicDataState.isLoading || epicDataState.error) {
                    Box(
                        modifier = commonModifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (epicDataState.error) {
                            Text(
                                text = "${epicDataState.statusCode}\n${epicDataState.statusDescription}",
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            ContainedLoadingIndicator()
                        }
                    }
                }
            }
            item {
                AnimatedContent(targetState = !epicDataState.error && !epicDataState.isLoading) {
                    if (it) {
                        HorizontalPager(
                            state = horizontalPager,
                        ) { epicItemIndex ->
                            Column {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(epicDataState.data[epicItemIndex].imageURL)
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    modifier = commonModifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(15.dp))
                                        .clickable {

                                        },
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                }
                if (currentEPICCapturedTime.isNotBlank()) {
                    if (!epicDataState.isLoading && !epicDataState.error) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(
                                    state = rememberScrollState()
                                )
                                .padding(start = 15.dp, end = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            epicDataState.data.forEach {
                                key(it.timeWhenImageWasCaptured) {
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 5.dp, end = 5.dp)
                                            .height(5.dp)
                                            .width(if (it.timeWhenImageWasCaptured == currentEPICCapturedTime) 50.dp else 35.dp)
                                            .clip(RoundedCornerShape(25.dp))
                                            .background(if (it.timeWhenImageWasCaptured == currentEPICCapturedTime) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(modifier = commonModifier) {
                        HeadlineDetailComponent(
                            string = currentEPICCapturedTime,
                            imageVector = Icons.Outlined.AccessTime,
                            fontSize = 14.sp,
                            iconSize = 20.dp
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row {
                        LabelValueCard(
                            outerPaddingValues = PaddingValues(start = 10.dp),
                            title = "Earth-Epic Distance",
                            value = currentDistanceToEarthFromTheEPIC
                        )
                        LabelValueCard(
                            outerPaddingValues = PaddingValues(start = 10.dp),
                            title = "Sun-Epic Distance",
                            value = currentDistanceToSunFromEPIC
                        )
                    }
                    ImageActionsRow(
                        openInBrowserURL = epicDataState.data[horizontalPager.currentPage].imageURL,
                        supportsBothHDDAndSD = false,
                        hdURL = null,
                        sdURL = epicDataState.data[horizontalPager.currentPage].imageURL,
                        sdDownloadDesc = "EPIC: Downloading image (${epicDataState.data[horizontalPager.currentPage].date}, ${epicDataState.data[horizontalPager.currentPage].timeWhenImageWasCaptured})",
                        hdDownloadDesc = null
                    )
                }
            }

            item {
                Spacer(Modifier.height(15.dp))
                HorizontalDivider(commonModifier)
                Spacer(Modifier.height(15.dp))
            }

            item {
                AnimatedContent(
                    contentKey = {
                        it
                    },
                    targetState = apodDataState.error || apodDataState.isLoading || apodImageURL.isNotBlank()
                ) {
                    Column {
                        if (it) {
                            Label(text = "APOD", modifier = commonModifier)
                            Spacer(Modifier.height(5.dp))
                            Text(
                                modifier = commonModifier,
                                text = "Astronomy Picture of the Day",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
            }

            item {
                AnimatedContent(targetState = apodDataState.isLoading || apodDataState.error) {
                    if (it) {
                        Box(
                            modifier = commonModifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (apodDataState.isLoading) {
                                ContainedLoadingIndicator()
                            } else if (apodDataState.error) {
                                Text(
                                    text = "${apodDataState.statusCode}\n${apodDataState.statusDescription}",
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    } else {
                        Column(
                            if (apodDataState.apod.second.isNotEmpty()) {
                            Modifier.drawWithCache {
                                onDrawWithContent {
                                    drawRect(
                                        alpha = 0.115f, brush = Brush.linearGradient(
                                            colors = apodDataState.apod.second
                                        )
                                    )
                                    drawContent()
                                }
                            }
                        } else {
                            Modifier
                        }.animateContentSize()) {
                            AsyncImage(
                                imageLoader = ImageLoader.Builder(LocalContext.current)
                                    .components {
                                        if (SDK_INT >= 28) {
                                            add(AnimatedImageDecoder.Factory())
                                        } else {
                                            add(GifDecoder.Factory())
                                        }
                                    }
                                    .build(),
                                model = ImageRequest.Builder(context).data("https://static.scientificamerican.com/sciam/assets/Image/2019/spinningblackhole.gif").crossfade(true).build(),
                                contentDescription = null,
                                modifier = commonModifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(15.dp))
                                    .border(
                                        1.5.dp,
                                        LocalContentColor.current.copy(0.25f),
                                        RoundedCornerShape(15.dp)
                                    ),
                                contentScale = ContentScale.Crop)

                            if (apodDataState.apod.first.date.trim().isNotBlank()) {
                                Spacer(Modifier.height(15.dp))
                                Box(modifier = commonModifier) {
                                    HeadlineDetailComponent(
                                        string = apodDataState.apod.first.date.trim(),
                                        imageVector = Icons.Outlined.CalendarToday,
                                        fontSize = 14.sp,
                                        iconSize = 20.dp
                                    )
                                }
                            }

                            if (apodDataState.apod.first.copyright.trim().isNotBlank()) {
                                Spacer(Modifier.height(5.dp))
                                Box(commonModifier) {
                                    HeadlineDetailComponent(
                                        string = apodDataState.apod.first.copyright.trim()
                                            .replace("\n", ""),
                                        imageVector = Icons.Outlined.Copyright,
                                        fontSize = 14.sp,
                                        iconSize = 20.dp
                                    )
                                }
                            }
                            ImageActionsRow(
                                openInBrowserURL = "https://apod.nasa.gov",
                                supportsBothHDDAndSD = true,
                                hdURL = apodDataState.apod.first.hdUrl,
                                sdURL = apodDataState.apod.first.url,
                                sdDownloadDesc = "APOD: Downloading SD image (${apodDataState.apod.first.date})",
                                hdDownloadDesc = "APOD: Downloading HD image (${apodDataState.apod.first.date})"
                            )
                            if (apodDataState.apod.first.title.trim().isNotBlank()) {
                                Spacer(Modifier.height(15.dp))
                                Text(
                                    modifier = commonModifier,
                                    text = "Title",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                SelectionContainer {
                                    Text(
                                        apodDataState.apod.first.title.trim(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 16.sp,
                                        modifier = commonModifier
                                    )
                                }
                            }

                            if (apodDataState.apod.first.explanation.trim().isNotBlank()) {
                                val isExplanationExpanded = rememberSaveable {
                                    mutableStateOf(false)
                                }
                                Spacer(Modifier.height(15.dp))
                                Text(
                                    modifier = commonModifier,
                                    text = "Explanation",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(Modifier.height(2.dp))
                                SelectionContainer(
                                    modifier = commonModifier
                                ) {
                                    Text(
                                        text = apodDataState.apod.first.explanation.trim(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontSize = 16.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = if (isExplanationExpanded.value) Int.MAX_VALUE else 3,
                                        modifier = Modifier
                                            .pulsateEffect()
                                            .clickable(onClick = {
                                                isExplanationExpanded.value =
                                                    !isExplanationExpanded.value
                                            }, indication = null, interactionSource = remember {
                                                MutableInteractionSource()
                                            })
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(15.dp))
                HorizontalDivider(commonModifier)
                Spacer(Modifier.height(15.dp))
            }
            item {
                Label(text = "Headlines", modifier = commonModifier)
            }
            if (topHeadlinesState.error) {
                item {
                    Box(
                        modifier = commonModifier
                            .padding(bottom = 150.dp, top = 15.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${topHeadlinesState.statusCode}\n${apodDataState.statusDescription}",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                return@LazyColumn
            }
            items(items = topHeadlinesState.data) { (headline, colors) ->
                TopHeadlineComponent(
                    article = Article(
                        author = headline.author,
                        content = headline.content,
                        description = headline.description,
                        publishedAt = headline.publishedAt,
                        source = Source(id = "", name = headline.sourceName),
                        title = headline.title,
                        url = headline.url,
                        urlToImage = headline.imageUrl
                    ),
                    colors = colors,
                    onItemClick = {
                        navController.navigate(
                            HyleNavigation.Headlines.TopHeadlineDetailScreen(
                                encodedString = Json.encodeToString(
                                    headline
                                )
                            )
                        )
                    },
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
            item {
                AnimatedContent(
                    contentKey = {
                    it
                },
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    modifier = Modifier.padding(
                        start = 15.dp, end = 15.dp, top = 15.dp, bottom = 150.dp
                    ),
                    targetState = topHeadlinesState.reachedMaxHeadlines to topHeadlinesState.isLoading
                ) {
                    if (it.second) {
                        LinearWavyProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (it.first) {
                        Text(
                            text = "That's all the headlines available at this time.",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        LaunchedEffect(lazyColumnState.canScrollForward) {
            if (!lazyColumnState.canScrollForward && !topHeadlinesState.reachedMaxHeadlines && !topHeadlinesState.isLoading && !topHeadlinesState.error) {
                homeScreenViewModel.retrievePaginatedTopHeadlines(context)
            }
        }
    }
}