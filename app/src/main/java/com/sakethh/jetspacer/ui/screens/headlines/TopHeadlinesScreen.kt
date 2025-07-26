package com.sakethh.jetspacer.ui.screens.headlines

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.domain.model.article.Article
import com.sakethh.jetspacer.domain.model.article.Source
import com.sakethh.jetspacer.ui.screens.headlines.components.TopHeadlineComponent
import com.sakethh.jetspacer.ui.navigation.HyleNavigation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlinesScreen(navController: NavController) {
    val topHeadlinesScreenViewModel = viewModel<TopHeadlinesScreenViewModel>()
    val topHeadlinesState = topHeadlinesScreenViewModel.topHeadLinesState
    val lazyColumnState = rememberLazyListState()
    Scaffold(topBar = {
        Column {
            TopAppBar(title = {
                Text(
                    text = "Top Headlines",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp)
                )
            }, actions = {
                IconButton(onClick = {
                    topHeadlinesScreenViewModel.clearCacheAndRefresh()
                }) {
                    Icon(imageVector = Icons.Default.ClearAll, contentDescription = null)
                }
            })
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.25f))
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = lazyColumnState
        ) {
            items(items = topHeadlinesState.value.data) { headline ->
                val icon =
                    if (headline.isBookmarked) Icons.Filled.BookmarkRemove else Icons.Outlined.BookmarkAdd
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
                    onImgClick = {

                    },
                    onItemClick = {
                        navController.navigate(
                            HyleNavigation.Headlines.TopHeadlineDetailScreenRoute(
                                encodedString = Json.encodeToString(
                                    headline
                                )
                            )
                        )
                    }
                )
            }
            if (topHeadlinesState.value.isLoading && topHeadlinesState.value.reachedMaxHeadlines.not() && topHeadlinesState.value.error.not()) {
                item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            if (topHeadlinesState.value.error) {
                item {
                    Text(
                        text = "${topHeadlinesState.value.statusCode}\n${topHeadlinesState.value.statusDescription}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }
            if (topHeadlinesState.value.reachedMaxHeadlines) {
                item {
                    Text(
                        text = "That's all the data found.",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                }
            }
            item {
                Spacer(Modifier.height(150.dp))
            }
        }
        LaunchedEffect(lazyColumnState.canScrollForward) {
            if (lazyColumnState.canScrollForward.not() && topHeadlinesState.value.reachedMaxHeadlines.not() && topHeadlinesState.value.isLoading.not() && topHeadlinesState.value.error.not()) {
                topHeadlinesScreenViewModel.retrievePaginatedTopHeadlines()
            }
        }
    }
}