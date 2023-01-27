package com.sakethh.jetspacer.screens.bookMarks.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.navigation.NavigationRoutes
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
            navController.navigate(route = NavigationRoutes.HOME_SCREEN)
        }
    }
    val newsBottomSheetContentImpl = NewsBottomSheetContentImpl()
    AppTheme {
        ModalBottomSheetLayout(sheetContent = {
            NewsBottomSheetContent(newsBottomSheetContentImpl = newsBottomSheetContentImpl)
        }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                this.newsUI(
                    bottomSheetState = bottomSheetState,
                    bookMarkedData = bookMarkedData,
                    coroutineScope = coroutineScope,
                    newsBottomSheetContentImpl = newsBottomSheetContentImpl
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