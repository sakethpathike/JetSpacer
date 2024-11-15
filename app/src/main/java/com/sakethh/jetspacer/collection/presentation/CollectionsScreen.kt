package com.sakethh.jetspacer.collection.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.collection.domain.CollectionType
import com.sakethh.jetspacer.headlines.domain.model.Article
import com.sakethh.jetspacer.headlines.domain.model.Source
import com.sakethh.jetspacer.headlines.presentation.components.TopHeadlineComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(navController: NavController) {
    val collectionsScreenViewModel: CollectionsScreenViewModel = viewModel()
    val bookMarkedTopHeadlines =
        collectionsScreenViewModel.bookMarkedTopHeadlinesData.collectAsStateWithLifecycle()
    val scrollableTabData = collectionsScreenViewModel.collectionTabData
    val pagerState = rememberPagerState(pageCount = { scrollableTabData.size })
    val selectedPageName = rememberSaveable {
        mutableStateOf(scrollableTabData.first().name)
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        Column {
            TopAppBar(title = {
                Text(
                    text = "Collection",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp)
                )
            })
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage, modifier = Modifier.fillMaxWidth()
            ) {
                scrollableTabData.forEachIndexed { index, tab ->
                    Tab(selected = tab.name == selectedPageName.value, onClick = {
                        selectedPageName.value = tab.name
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }) {
                        Text(
                            text = tab.name,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
            HorizontalPager(state = pagerState) { page ->
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    when (scrollableTabData[page].type) {
                        CollectionType.APOD_Archive -> {

                        }

                        CollectionType.Mars_Gallery -> {

                        }

                        CollectionType.News -> {
                            items(bookMarkedTopHeadlines.value) { headline ->
                                TopHeadlineComponent(
                                    article = Article(
                                        author = headline.author,
                                        content = headline.content,
                                        description = headline.description,
                                        publishedAt = headline.publishedAt,
                                        source = Source(
                                            id = "https://tidal.com/browse/playlist/e074ab28-42a8-4c19-a853-97544166c201",
                                            name = headline.sourceName
                                        ),
                                        title = headline.title,
                                        url = headline.url,
                                        urlToImage = headline.imageUrl
                                    ),
                                    onImgClick = {

                                    },
                                    onItemClick = {

                                    },
                                    onBookMarkClick = {

                                    },
                                    bookMarkIcon = Icons.Filled.BookmarkAdded
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

