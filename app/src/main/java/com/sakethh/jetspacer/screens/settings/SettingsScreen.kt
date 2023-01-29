package com.sakethh.jetspacer.screens.settings

import android.content.ClipDescription
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContentImpl
import com.sakethh.jetspacer.screens.webview.WebViewUtils
import com.sakethh.jetspacer.screens.webview.enableBtmBarInWebView
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    dataStore: DataStore<Preferences>,
    navController: NavController,
) {
    BackHandler {
        navController.navigate(NavigationRoutes.HOME_SCREEN) {
            popUpTo(0)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val bookMarksVM: BookMarksVM = viewModel()
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .animateContentSize()
        ) {
            item {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate(NavigationRoutes.HOME_SCREEN)
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
                    },
                    title = {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.requiredHeight(45.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Open web links in-app",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 15.dp, top = 0.dp)
                        )
                    }
                    Box(contentAlignment = Alignment.Center) {
                        Switch(checked = Settings.inAppBrowserSetting.value,
                            modifier = Modifier.padding(end = 20.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                checkedThumbColor = MaterialTheme.colorScheme.onSurface
                            ),
                            onCheckedChange = {
                                coroutineScope.launch {
                                    changeInAppBrowserSetting(
                                        isInAppBrowsing = !Settings.inAppBrowserSetting.value,
                                        dataStore = dataStore
                                    )
                                    readInAppBrowserSetting(dataStore = dataStore)
                                }
                            },
                            thumbContent = {
                                if (Settings.inAppBrowserSetting.value) {
                                    Image(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "close icon",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            })
                    }
                }
            }
            item {
                DividerComposable()
            }
            item {
                Text(
                    text = "Storage",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.titlePadding()
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.clickable {
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value =true
                    },
                    title = "Clear Bookmarks",
                    description = "Remove all bookmarks from local database. Cache won't be removed."
                )
            }
            item {
                DividerComposable()
            }
            item {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.titlePadding()
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier,
                    title = "Privacy",
                    description = "Every bit of data is stored locally on your device!"
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        newsBottomSheetContentImpl =NewsBottomSheetContentImpl(sourceURL = "https://github.com/sakethpathike/JetSpacer"),
                        {},inSettingsScreen = true
                    ),
                    title = "Source code",
                    description = "The code-base for this android client is public and open-source, checkout repository for further information on how this app works and what tech have been used to make this app alive *_*"
                )
            }
            item {
                DividerComposable()
            }
            item {
                Text(
                    text = "Social",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.titlePadding()
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        newsBottomSheetContentImpl =NewsBottomSheetContentImpl(sourceURL = "https://twitter.com/jetspacerapp"),
                        {},inSettingsScreen = true
                    ),
                    title = "Twitter",
                    description = "Checkout \"Jet Spacer\" on Twitter for latest updates or anything related to this project"
                )
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.SAVED_IN_NEWS_DB,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        awaitAll(async {
                            bookMarksVM.dbImplementation.localDBData().deleteAllDataFromAPODDB()
                        }, async {
                            bookMarksVM.dbImplementation.localDBData().deleteAllDataFromMarsDB()
                        }, async {
                            bookMarksVM.dbImplementation.localDBData().deleteAllDataFromNewsDB()
                        })
                    }.invokeOnCompletion {
                        Toast.makeText(context, "Deleted all bookmarks:)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
                    HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                }
            )
        }
    }
}

fun Modifier.titlePadding(): Modifier {
    return this.padding(start = 15.dp, top = 25.dp, end = 20.dp)
}

fun Modifier.descriptionPadding(): Modifier {
    return this.padding(start = 15.dp, top = 8.dp, end = 20.dp)
}

@Composable
fun DividerComposable() {
    Divider(
        thickness = 1.dp,
        modifier = Modifier.padding(start = 25.dp, end = 25.dp, top = 25.dp, bottom = 0.dp),
        color = Color.DarkGray
    )
}

suspend fun changeInAppBrowserSetting(
    settingsPreferenceKey: Preferences.Key<Boolean> = preferencesKey(
        "settingsPreferences"
    ),
    dataStore: DataStore<Preferences>, isInAppBrowsing: Boolean,
) {
    dataStore.edit {
        it[settingsPreferenceKey] = isInAppBrowsing
    }
}

suspend fun readInAppBrowserSetting(
    dataStore: DataStore<Preferences>,
    settingsPreferenceKey: Preferences.Key<Boolean> = preferencesKey(
        "settingsPreferences"
    ),
) {
    val preference = dataStore.data.first()[settingsPreferenceKey]
    if (preference != null) {
        Settings.inAppBrowserSetting.value = preference
    }
}

object Settings {
    val inAppBrowserSetting = mutableStateOf(true)
}

fun Modifier.redirectToWeb(
    navController: NavController, newsBottomSheetContentImpl: NewsBottomSheetContentImpl,onClick:()->Unit,inSettingsScreen:Boolean=false
): Modifier = composed {
    val localUriHandler = LocalUriHandler.current
    this.clickable {
        onClick()
        enableBtmBarInWebView=!inSettingsScreen
        if (Settings.inAppBrowserSetting.value) {
            WebViewUtils.newsBottomSheetContentImpl = newsBottomSheetContentImpl
            navController.navigate(NavigationRoutes.WEB_VIEW_SCREEN)
        } else {
            localUriHandler.openUri(newsBottomSheetContentImpl.sourceURL)
        }
    }
}

@Composable
fun IndividualSettingItemComposable(
    modifier: Modifier, title: String, description: String, lineHeight: TextUnit = 18.sp,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.titlePadding()
        )
        Text(
            text = description,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.descriptionPadding(),
            lineHeight = lineHeight
        )
    }
}