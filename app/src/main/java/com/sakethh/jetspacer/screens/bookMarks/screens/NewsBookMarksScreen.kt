package com.sakethh.jetspacer.screens.bookMarks.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContent
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContentImpl
import com.sakethh.jetspacer.screens.news.newsUI
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsBookmarkScreen(navController: NavController) {
    val bookMarksVM: BookMarksVM = viewModel()
    val bookMarkedData = bookMarksVM.bookMarksFromNewsDB.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context= LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    BackHandler {
        if(bottomSheetState.isVisible){
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }else{
            navController.navigate(route = NavigationRoutes.HOME_SCREEN) {
                popUpTo(0)
            }
        }
    }
    val newsBottomSheetContentImpl = NewsBottomSheetContentImpl()
    AppTheme {
        ModalBottomSheetLayout(sheetShape =
        RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp),
            sheetState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    NewsBottomSheetContent(newsBottomSheetContentImpl = newsBottomSheetContentImpl)
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        ) {
            if (bookMarkedData.value.isEmpty()) {
                StatusScreen(
                    title = "No bookmarks found",
                    description = "Add images to your NEWS bookmarks as follows:\n1. Click on more icon in news screen for the news respectively\n2. Add to bookmark\nit's that simple:)",
                    status = Status.BOOKMARKS_EMPTY
                )
            } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                this.newsUI(
                    bottomSheetState = bottomSheetState,
                    bookMarkedData = bookMarkedData,
                    coroutineScope = coroutineScope,
                    newsBottomSheetContentImpl = newsBottomSheetContentImpl,
                    navController = navController,
                    bookMarksVM = bookMarksVM
                )
                item {
                    Spacer(modifier = Modifier.height(75.dp))
                }
            }}
        }

        var doesExistsInDB = false
        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    coroutineScope.launch {
                        doesExistsInDB =
                            bookMarksVM.deleteDataFromNewsDB(sourceURL = newsBottomSheetContentImpl.sourceURL)
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
                        bookMarksVM.doesThisExistsInNewsDBIconTxt(sourceURL = newsBottomSheetContentImpl.sourceURL)
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                }
            )
        }
    }
}