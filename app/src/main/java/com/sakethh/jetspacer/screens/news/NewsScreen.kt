package com.sakethh.jetspacer.screens.news

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.NewsDB
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.dto.Article
import com.sakethh.jetspacer.ui.theme.AppTheme
import io.ktor.http.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NewsScreen() {
    val newsVM: NewsVM = viewModel()
    val topHeadLinesData = newsVM.topHeadLinesListFromAPI
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val shouldLoadMoreData = remember { mutableStateOf(false) }
    val isRefreshing = remember { mutableStateOf(false) }
    val newsBottomSheetContentImpl = NewsBottomSheetContentImpl().copy()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val isConnectedToInternet =
        HomeScreenViewModel.Network.connectedToInternet.collectAsState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                coroutineScope.launch {
                    awaitAll(
                        async {
                            withContext(Dispatchers.Main) {
                                if (isConnectedToInternet.value) {
                                    Toast.makeText(
                                        context,
                                        "Refreshing data in a moment",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                delay(2000L)
                                isRefreshing.value = false
                            }
                        },
                        async {
                            try {
                                HomeScreenViewModel.Network.isConnectionSucceed.value =
                                    true
                                newsVM.reloadTopHeadLinesData()
                            } catch (_: Exception) {
                                HomeScreenViewModel.Network.isConnectionSucceed.value =
                                    false
                                withContext(Dispatchers.Main) {
                                    isRefreshing.value = false
                                    Toast.makeText(
                                        context,
                                        "Network not detected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
            })
    AppTheme {
        ModalBottomSheetLayout(sheetContent = {
            NewsBottomSheetContent(newsBottomSheetContentImpl = newsBottomSheetContentImpl)
        }) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(
                                text = "Top Headlines",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                }) {
                if (topHeadLinesData.value.isEmpty()) {
                    Column {
                        Spacer(modifier = Modifier.height(70.dp))
                        val status = if (isConnectedToInternet.value) {
                            Status.LOADING
                        } else {
                            Status.NO_INTERNET
                        }
                        StatusScreen(
                            title = "Wait a moment!",
                            description = "Fetching the latest space-related top headlines from around the globe",
                            status = status
                        )
                    }
                } else {
                    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
                        LazyColumn(
                            contentPadding = it,
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            this.newsUI(
                                topHeadLinesData = topHeadLinesData,
                                bottomSheetState = modalBottomSheetState,
                                coroutineScope = coroutineScope,
                                newsBottomSheetContentImpl = newsBottomSheetContentImpl
                            )
                            item {
                                Button(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    onClick = {
                                        shouldLoadMoreData.value = false
                                        if (shouldLoadMoreData.value) {
                                            coroutineScope.launch {
                                                newsVM.loadTopHeadLinesData()
                                            }.invokeOnCompletion {
                                                shouldLoadMoreData.value = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    if (shouldLoadMoreData.value) {
                                        Text(
                                            text = "Load more news",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else if (topHeadLinesData.value.count() == newsVM.totalNewsCount.value) {
                                        Text(
                                            text = "You've came to end!",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else if (!shouldLoadMoreData.value) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            Text(
                                                text = "Loading more news...",
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(10.dp),
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        PullRefreshIndicator(
                            refreshing = isRefreshing.value,
                            state = pullRefreshState,
                            Modifier.align(Alignment.TopCenter),
                            scale = true
                        )
                    }
                }
            }
        }

        val bookMarksVM: BookMarksVM = viewModel()
        var doesExistsInDB = false
        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        doesExistsInDB =
                            bookMarksVM.deleteDataFromNewsDB(imageURL = newsBottomSheetContentImpl.imageURL)
                    }.invokeOnCompletion {
                        if (doesExistsInDB) {
                            Toast.makeText(
                                context,
                                "Bookmark didn't got removed as expected, report it:(",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Removed from bookmarks:)",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        bookMarksVM.doesThisExistsInNewsDBIconTxt(imageURL = newsBottomSheetContentImpl.imageURL)
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.newsUI(
    bottomSheetState: ModalBottomSheetState,
    topHeadLinesData: MutableState<List<Article>>,
    coroutineScope: CoroutineScope,
    newsBottomSheetContentImpl: NewsBottomSheetContentImpl
) {
    items(topHeadLinesData.value) {
        Row(
            modifier = Modifier
                .padding(top = 15.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                Text(
                    text = it.title,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(
                        top = 15.dp,
                        start = 15.dp,
                        end = 15.dp
                    ),
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp,
                    maxLines = 4,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 1
                )
                Text(
                    text = it.source.name,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 20.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = it.publishedAt,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 10.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column {
                Box {
                    Coil_Image().CoilImage(
                        imgURL = it.urlToImage,
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .border(
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(
                                    0.dp,
                                    Color.Transparent
                                )
                            )
                            .align(Alignment.CenterEnd),
                        onError = painterResource(id = R.drawable.baseline_image_24)
                    )
                    IconButton(onClick = {
                        newsBottomSheetContentImpl.imageURL = it.urlToImage
                        newsBottomSheetContentImpl.publishedTime = it.publishedAt
                        newsBottomSheetContentImpl.sourceName = it.source.name
                        newsBottomSheetContentImpl.sourceURL = it.url
                        newsBottomSheetContentImpl.title = it.title
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
    item {
        Spacer(modifier = Modifier.height(75.dp))
    }
}

data class NewsBottomSheetContent(val title: String, val icon: ImageVector, val onClick: () -> Unit)

data class NewsBottomSheetContentImpl(
    var imageURL: String = "",
    var sourceName: String = "",
    var title: String = "",
    var publishedTime: String = "",
    var sourceURL: String = ""
)

@Composable
fun NewsBottomSheetContent(newsBottomSheetContentImpl: NewsBottomSheetContentImpl) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bookMarksVM: BookMarksVM = viewModel()
    val localURI = LocalUriHandler.current
    val localClipBoard = LocalClipboardManager.current
    var didDataGetAddedInDB = false
    bookMarksVM.doesThisExistsInNewsDBIconTxt(imageURL = newsBottomSheetContentImpl.imageURL)
    val bottomList = listOf(
        NewsBottomSheetContent(
            title = bookMarksVM.bookMarkText.value,
            icon = bookMarksVM.bookMarkIcons.value,
            onClick = {
                triggerHapticFeedback(context = context)
                coroutineScope.launch {
                    didDataGetAddedInDB = bookMarksVM.addDataToNewsDB(NewsDB().apply {
                        this.title = newsBottomSheetContentImpl.title
                        this.imageURL = newsBottomSheetContentImpl.imageURL
                        this.sourceOfNews = newsBottomSheetContentImpl.sourceName
                        this.publishedTime = newsBottomSheetContentImpl.publishedTime
                        this.sourceURL = newsBottomSheetContentImpl.sourceURL
                    })
                }.invokeOnCompletion {
                    if (didDataGetAddedInDB) {
                        Toast.makeText(
                            context,
                            "Added to bookmarks:)",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                            true
                    }
                    bookMarksVM.doesThisExistsInNewsDBIconTxt(bookMarksVM.imgURL)
                }
            }),
        NewsBottomSheetContent(
            title = "Open in browser",
            icon = Icons.Default.OpenInBrowser,
            onClick = {
                localURI.openUri(newsBottomSheetContentImpl.sourceURL)
            }),
        NewsBottomSheetContent(
            title = "Copy news source link",
            icon = Icons.Default.ContentCopy,
            onClick = {
                localClipBoard.setText(AnnotatedString(newsBottomSheetContentImpl.sourceURL))
            }),
        NewsBottomSheetContent(
            title = "Share",
            icon = Icons.Default.Share,
            onClick = {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Checkout top-headline:\n${newsBottomSheetContentImpl.title}\nsource:\n${newsBottomSheetContentImpl.sourceURL}"
                )
                val activity = context as Activity?
                activity?.startActivity(intent)
            }),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        bottomList.forEach {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { it.onClick() }
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                androidx.compose.material.Icon(
                    imageVector = it.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = it.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    }
}
