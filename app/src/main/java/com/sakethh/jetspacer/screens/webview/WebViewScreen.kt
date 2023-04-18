package com.sakethh.jetspacer.screens.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.localDB.NewsDB
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.triggerHapticFeedback
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContent
import com.sakethh.jetspacer.screens.news.NewsBottomSheetMutableStateDTO
import com.sakethh.jetspacer.screens.webview.WebViewUtils.newsBottomSheetContentImpl
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

object WebViewUtils {
    var newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf("")
    )
}

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WebViewScreen(navController: NavController) {
    val bookMarksVM: BookMarksVM = viewModel()
    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
    val webViewState = rememberWebViewState(url = newsBottomSheetContentImpl.sourceURL.value)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var doDataExistsInDB = false
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val localClipBoard = LocalClipboardManager.current
    val bottomList = listOf(
        NewsBottomSheetContent(
            title = bookMarksVM.bookMarkText.value,
            icon = bookMarksVM.bookMarkIcons.value,
            onClick = {
                triggerHapticFeedback(context = context)
                coroutineScope.launch {
                    doDataExistsInDB = BookMarksVM.dbImplementation.localDBData()
                        .doesThisExistsInNewsDB(newsBottomSheetContentImpl.sourceURL.value)
                }.invokeOnCompletion {
                    if (!doDataExistsInDB) {
                        coroutineScope.launch {
                            bookMarksVM.addDataToNewsDB(newsDB = NewsDB().apply {
                                this.title = newsBottomSheetContentImpl.title.value
                                this.imageURL = newsBottomSheetContentImpl.imageURL.value
                                this.sourceOfNews = newsBottomSheetContentImpl.sourceName.value
                                this.publishedTime = newsBottomSheetContentImpl.publishedTime.value
                                this.sourceURL = newsBottomSheetContentImpl.sourceURL.value
                            })
                        }.invokeOnCompletion {
                            bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                        }
                        Toast.makeText(
                            context,
                            "Added to bookmarks:)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =
                            true
                    }
                }
            }),
        NewsBottomSheetContent(
            title = "Copy news source link",
            icon = Icons.Default.ContentCopy,
            onClick = {
                localClipBoard.setText(AnnotatedString(newsBottomSheetContentImpl.sourceURL.value))
            }),
        NewsBottomSheetContent(
            title = "Share",
            icon = Icons.Default.Share,
            onClick = {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Checkout top-headline:\n${newsBottomSheetContentImpl.title.value}\nsource:\n${newsBottomSheetContentImpl.sourceURL.value}"
                )
                intent.type = "text/plain"
                val activity = context as Activity?
                activity?.startActivity(intent)
            }),
        NewsBottomSheetContent(
            title = "More",
            icon = Icons.Default.MoreVert,
            onClick = {
                bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                coroutineScope.launch {
                    modalBottomSheetState.show()
                }
            })
    )
    BackHandler {
        if (modalBottomSheetState.isVisible) {
            coroutineScope.launch {
                modalBottomSheetState.hide()
            }
        } else {
            navController.popBackStack()
        }
    }
    AppTheme {
        ModalBottomSheetLayout(sheetShape =
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
            sheetState = modalBottomSheetState, sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                    NewsBottomSheetContent(newsBottomSheetMutableStateDTO = newsBottomSheetContentImpl)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }) {
            Scaffold(topBar = {
                TopAppBar(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface), title = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }, modifier = Modifier
                            .padding(start = 10.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                })
            }, bottomBar = {
                if (enableBtmBarInWebView) {
                    bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                    TopAppBar(actions = {
                        bottomList.forEach {
                            IconButton(
                                onClick = {
                                    it.onClick()
                                }, modifier = Modifier
                                    .padding(start = 10.dp)
                            ) {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }, title = {})
                }
            }) {
                WebView(
                    onCreated = { webView ->
                        webView.settings.javaScriptEnabled = true
                    },
                    state = webViewState, modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                )
            }
        }
        var doesExistsInDB = false
        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        doesExistsInDB =
                            bookMarksVM.deleteDataFromNewsDB(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
                    }.invokeOnCompletion {
                        bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL.value)
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
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                }
            )
        }
    }
}

var enableBtmBarInWebView = false