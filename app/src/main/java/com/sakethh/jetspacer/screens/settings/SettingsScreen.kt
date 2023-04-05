package com.sakethh.jetspacer.screens.settings

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.localDB.APIKeysDB
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.NewsBottomSheetContentImpl
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.webview.WebViewUtils
import com.sakethh.jetspacer.screens.webview.enableBtmBarInWebView
import com.sakethh.jetspacer.ui.theme.AppTheme
import io.ktor.client.request.*
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
    val newNasaAPIKey = rememberSaveable { mutableStateOf("") }
    val newNewsAPIKey = rememberSaveable { mutableStateOf("") }
    val existingNasaAPIKey = rememberSaveable { mutableStateOf("") }
    val existingNewsAPIKey = rememberSaveable { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        awaitAll(
            async { newNasaAPIKey.value = bookMarksVM.getApiKeys()[0].currentNASAAPIKey },
            async { newNewsAPIKey.value = bookMarksVM.getApiKeys()[0].currentNewsAPIKey },
            async { existingNasaAPIKey.value = bookMarksVM.getApiKeys()[0].currentNASAAPIKey },
            async { existingNewsAPIKey.value = bookMarksVM.getApiKeys()[0].currentNewsAPIKey })
    }
    var isApiKeyValid = false
    val isVerifyingNewApiKeyDialogActive = rememberSaveable { mutableStateOf(false) }
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
                    text = "API Usage",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.titlePadding()
                )
            }
            item {
                Text(
                    text = "\"JetSpacer\" is fueled by a bunch of APIs, two of which are NASA's open API and NewsApi.org's API, which are necessary in order to run this app without any destruction. This project uses free tier API access to both of the APIs, which can be exhausted at any time. It is recommended to grab your own or existing API key(s) for both of the APIs in order to avoid any destruction while using this app!\n",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.descriptionPadding(),
                    lineHeight = 18.sp
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Current NASA-API Key",
                    description = existingNasaAPIKey.value
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Current NEWS-API Key",
                    description = existingNewsAPIKey.value
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .redirectToWeb(
                            navController = navController,
                            inSettingsScreen = true,
                            newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://2fpvxo3g.bearblog.dev/nasa-api-keys/"),
                            onClick = {})
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    title = "Grab NASA API Keys",
                    description = "Click \uD83C\uDF1A"
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .redirectToWeb(
                            navController = navController,
                            inSettingsScreen = true,
                            newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://2fpvxo3g.bearblog.dev/news-apis-api-keys/"),
                            onClick = {})
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    title = "Grab NEWS API Keys",
                    description = "Click \uD83C\uDF1A"
                )
            }
            item {
                DividerComposable()
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "API Keys",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.titlePadding()
                    )
                    Text(
                        text = "Reset API Keys",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.titlePadding().background(MaterialTheme.colorScheme.primary).clickable {
                            coroutineScope.launch {
                                BookMarksVM.dbImplementation.localDBData()
                                    .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                                        this.currentNASAAPIKey = Constants.NASA_APIKEY
                                        this.currentNewsAPIKey = Constants.NEWS_API_API_KEY
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                Toast.makeText(
                                    context,
                                    "Changed API Keys to default , this may break app purpose at any time:(",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }

            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        inSettingsScreen = true,
                        newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://2fpvxo3g.bearblog.dev/nasa-api-keys/"),
                        onClick = {}),
                    title = "NASA API-KEY",
                    description = "Enter the API key in the text box below; this key will be used to access NASA's API. Your key is stored on the local device itself:)\nPsst: JetSpacer will verify if your entered api key is valid or not to avoid further destruction, your welcome."
                )
            }
            item {
                SolTextField(onContinueClick = {
                    isVerifyingNewApiKeyDialogActive.value = true
                    coroutineScope.launch {
                        isApiKeyValid = try {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = true
                            HTTPClient.ktorClientWithoutCache.get("https://api.nasa.gov/planetary/apod?api_key=${newNasaAPIKey.value}").status.value == 200
                        } catch (_: Exception) {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = false
                            false
                        }
                    }.invokeOnCompletion {
                        if (isApiKeyValid) {
                            coroutineScope.launch {
                                BookMarksVM.dbImplementation.localDBData()
                                    .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                                        this.currentNASAAPIKey = newNasaAPIKey.value
                                        this.currentNewsAPIKey = existingNewsAPIKey.value
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                coroutineScope.launch {
                                    newNasaAPIKey.value =
                                        bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    newNewsAPIKey.value =
                                        bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                }.invokeOnCompletion {
                                    isVerifyingNewApiKeyDialogActive.value = false
                                    Toast.makeText(
                                        context,
                                        "Updated NASA API Key without any failure",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            isVerifyingNewApiKeyDialogActive.value = false
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        newNasaAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    },
                                    async {
                                        newNewsAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                    },
                                    async {
                                        existingNasaAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    },
                                    async {
                                        existingNewsAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                    })
                            }
                            Toast.makeText(
                                context,
                                "Oh-uh, Entered NASA API Key is invalid:(",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, solValue = newNasaAPIKey, inSettingsScreen = true)
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        inSettingsScreen = true,
                        newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://2fpvxo3g.bearblog.dev/news-apis-api-keys/"),
                        onClick = {}),
                    title = "NEWS API-KEY",
                    description = "Enter the API key in the text box below; this key will be used to access NewsApi.org's API. Your key is stored on the local device itself:)\nPsst: JetSpacer will verify if your entered api key is valid or not to avoid further destruction, your welcome."
                )
            }
            item {
                SolTextField(onContinueClick = {
                    isVerifyingNewApiKeyDialogActive.value = true
                    coroutineScope.launch {
                        isApiKeyValid = try {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = true
                            HTTPClient.ktorClientWithoutCache.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=1&page=1&apiKey=${newNewsAPIKey.value}").status.value == 200
                        } catch (_: Exception) {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = false
                            false
                        }
                    }.invokeOnCompletion {
                        if (isApiKeyValid) {
                            coroutineScope.launch {
                                BookMarksVM.dbImplementation.localDBData()
                                    .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                                        this.currentNewsAPIKey = newNewsAPIKey.value
                                        this.currentNASAAPIKey = existingNasaAPIKey.value
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                coroutineScope.launch {
                                    newNasaAPIKey.value =
                                        bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    newNewsAPIKey.value =
                                        bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                }.invokeOnCompletion {
                                    isVerifyingNewApiKeyDialogActive.value = false
                                    Toast.makeText(
                                        context,
                                        "Updated NEWS API Key without any failure",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            isVerifyingNewApiKeyDialogActive.value = false
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        newNasaAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    },
                                    async {
                                        newNewsAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                    },
                                    async {
                                        existingNasaAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    },
                                    async {
                                        existingNewsAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                    })
                            }
                            Toast.makeText(
                                context,
                                "Oh-uh, Entered NEWS API Key is invalid:(",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, solValue = newNewsAPIKey, inSettingsScreen = true)
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
                        HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = true
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
                        newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://github.com/sakethpathike/JetSpacer"),
                        {}, inSettingsScreen = true
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
                        newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://twitter.com/jetspacerapp"),
                        {}, inSettingsScreen = true
                    ),
                    title = "Twitter",
                    description = "Checkout \"Jet Spacer\" on Twitter for latest updates or anything related to this project"
                )
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .redirectToWeb(
                            navController = navController,
                            newsBottomSheetContentImpl = NewsBottomSheetContentImpl(sourceURL = "https://www.flaticon.com/authors/iconfield"),
                            inSettingsScreen = true,
                            onClick = {})
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                    ) {
                        Text(
                            text = "App Icon by Iconfield",
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.titlePadding(),
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "App icon originally created by Iconfield on Flaticon",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.descriptionPadding(),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
            AlertDialogForDeletingFromDB(
                bookMarkedCategory = Constants.IN_SETTINGS_SCREEN,
                onConfirmBtnClick = {
                    triggerHapticFeedback(context = context)
                    coroutineScope.launch {
                        awaitAll(async {
                            BookMarksVM.dbImplementation.localDBData().deleteAllDataFromAPODDB()
                        }, async {
                            BookMarksVM.dbImplementation.localDBData().deleteAllDataFromMarsDB()
                        }, async {
                            BookMarksVM.dbImplementation.localDBData().deleteAllDataFromNewsDB()
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
        if (isVerifyingNewApiKeyDialogActive.value) {
            androidx.compose.material3.AlertDialog(modifier = Modifier
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(100.dp),
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
                onDismissRequest = { isVerifyingNewApiKeyDialogActive.value = false },
                content = {
                    Row(
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 4.dp,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Wait a minute, verifying API Key",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 20.sp,
                            lineHeight = 22.sp,
                            softWrap = true,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                })

        }
    }
}

fun Modifier.titlePadding(): Modifier {
    return this.padding(start = 15.dp, top = 20.dp, end = 20.dp)
}

fun Modifier.descriptionPadding(): Modifier {
    return this.padding(start = 15.dp, top = 8.dp, end = 20.dp, bottom = 20.dp)
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
    navController: NavController,
    newsBottomSheetContentImpl: NewsBottomSheetContentImpl,
    onClick: () -> Unit,
    inSettingsScreen: Boolean = false,
): Modifier = composed {
    val localUriHandler = LocalUriHandler.current
    this.clickable {
        onClick()
        enableBtmBarInWebView = !inSettingsScreen
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