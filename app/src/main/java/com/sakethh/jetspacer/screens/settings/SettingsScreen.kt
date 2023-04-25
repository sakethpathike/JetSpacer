package com.sakethh.jetspacer.screens.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.sakethh.jetspacer.screens.home.AlertDialogForDeletingFromDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.triggerHapticFeedback
import com.sakethh.jetspacer.screens.news.NewsBottomSheetMutableStateDTO
import com.sakethh.jetspacer.screens.settings.Settings.selectedTheme
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.webview.WebViewUtils
import com.sakethh.jetspacer.screens.webview.enableBtmBarInWebView
import com.sakethh.jetspacer.ui.theme.AppTheme
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    dataStore: DataStore<Preferences>, navController: NavController,
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

    val existingIPGeolocationAPIKey = rememberSaveable { mutableStateOf("") }
    val newIPGeolocationAPIKey = rememberSaveable { mutableStateOf("") }

    val astronomicalDataFetchingRateLimitValue =
        rememberSaveable(Settings.astronomicalTimeInterval.value) { mutableStateOf(Settings.astronomicalTimeInterval.value) }
    val themeTypeForThemeDialog = listOf("Follow system theme", "Light", "Dark")

    LaunchedEffect(key1 = true) {
        awaitAll(
            async { newNasaAPIKey.value = bookMarksVM.getApiKeys()[0].currentNASAAPIKey },
            async { newNewsAPIKey.value = bookMarksVM.getApiKeys()[0].currentNewsAPIKey },
            async { existingNasaAPIKey.value = bookMarksVM.getApiKeys()[0].currentNASAAPIKey },
            async { existingNewsAPIKey.value = bookMarksVM.getApiKeys()[0].currentNewsAPIKey },
            async {
                existingIPGeolocationAPIKey.value =
                    bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
            },
            async {
                newIPGeolocationAPIKey.value =
                    bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
            }
        )
    }
    var isApiKeyValid = false
    val isVerifyingNewApiKeyDialogActive = rememberSaveable { mutableStateOf(false) }
    val isThemeChangeDialogEnabled = rememberSaveable { mutableStateOf(false) }
    val localURIHandler = LocalUriHandler.current
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.requiredHeight(45.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Theme",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 15.dp, top = 0.dp)
                        )
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                        .clickable {
                            isThemeChangeDialogEnabled.value = true
                        }
                        .wrapContentSize()) {
                        Button(
                            onClick = {
                                isThemeChangeDialogEnabled.value = !isThemeChangeDialogEnabled.value
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .padding(end = 15.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Change theme",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
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
                    text = "\"JetSpacer\" is fueled by a bunch of APIs, which are necessary in order to run this app without any destruction.\n\nThis project uses free tier API access to most of the APIs, which can be exhausted at any time. It is recommended to grab your own or existing API key(s) for the APIs in order to avoid any destruction while using this app!",
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
                        .fillMaxWidth(),
                    title = "Current IPGeolocation-API Key",
                    description = existingIPGeolocationAPIKey.value
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .redirectToWeb(
                            navController = navController,
                            inSettingsScreen = true,
                            newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                                sourceURL = mutableStateOf(
                                    "https://2fpvxo3g.bearblog.dev/nasa-api-keys/"
                                )
                            ),
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
                            newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                                sourceURL = mutableStateOf(
                                    "https://2fpvxo3g.bearblog.dev/news-apis-api-keys/"
                                )
                            ),
                            onClick = {})
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    title = "Grab NEWS API Keys",
                    description = "Click \uD83C\uDF1A"
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier
                        .redirectToWeb(
                            navController = navController,
                            inSettingsScreen = true,
                            newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                                sourceURL = mutableStateOf(
                                    "https://2fpvxo3g.bearblog.dev/ipgeolocation-apis-api-keys/"
                                )
                            ),
                            onClick = {})
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    title = "Grab IPGeolocation API Keys",
                    description = "Click \uD83C\uDF1A"
                )
            }
            item {
                DividerComposable()
            }
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your API key(s) will be stored in your device itself:)\n\nPsst: JetSpacer will verify if your entered api key is valid or not; to avoid further destruction, your welcome.",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.titlePadding(),
                        lineHeight = 20.sp
                    )
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
                        Button(
                            onClick = {
                                coroutineScope
                                    .launch {
                                        BookMarksVM.dbImplementation
                                            .localDBData()
                                            .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                                                this.currentNASAAPIKey = Constants.NASA_APIKEY
                                                this.currentNewsAPIKey =
                                                    Constants.NEWS_API_API_KEY
                                                this.currentIPGeoLocationAPIKey =
                                                    Constants.IP_GEOLOCATION_APIKEY
                                                this.id = "apiKey"
                                            })
                                    }
                                    .invokeOnCompletion {
                                        Toast
                                            .makeText(
                                                context,
                                                "Changed API Keys to default , this may break app purpose at any time:(",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .padding(top = 12.dp, end = 15.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Reset API Keys",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        inSettingsScreen = true,
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(

                            sourceURL = mutableStateOf("https://2fpvxo3g.bearblog.dev/nasa-api-keys/")

                        ),
                        onClick = {}).fillMaxWidth(),
                    title = "NASA API-KEY",
                    description = "Enter the API key in the text box below; this key will be used to access NASA's API."
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
                                        this.currentIPGeoLocationAPIKey =
                                            existingIPGeolocationAPIKey.value
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                coroutineScope.launch {
                                    newNasaAPIKey.value =
                                        bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                    existingNasaAPIKey.value = newNasaAPIKey.value
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
                                newNasaAPIKey.value =
                                    bookMarksVM.getApiKeys()[0].currentNASAAPIKey
                                existingNasaAPIKey.value = newNasaAPIKey.value
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
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                            sourceURL = mutableStateOf(
                                "https://2fpvxo3g.bearblog.dev/news-apis-api-keys/"
                            )
                        ),
                        onClick = {}).fillMaxWidth(),
                    title = "NEWS API-KEY",
                    description = "Enter the API key in the text box below; this key will be used to access NewsApi.org's API."
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
                                        this.currentIPGeoLocationAPIKey =
                                            existingIPGeolocationAPIKey.value
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                coroutineScope.launch {
                                    awaitAll(
                                        async {
                                            newNewsAPIKey.value =
                                                bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                        },
                                        async {
                                            existingNewsAPIKey.value =
                                                bookMarksVM.getApiKeys()[0].currentNewsAPIKey
                                        })
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
                                        newNewsAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentNewsAPIKey
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
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        inSettingsScreen = true,
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                            sourceURL = mutableStateOf(
                                "https://2fpvxo3g.bearblog.dev/ipgeolocation-apis-api-keys/"
                            )
                        ),
                        onClick = {}).fillMaxWidth(),
                    title = "IPGeolocation API-KEY",
                    description = "Enter the API key in the text box below; this key will be used to access IPGeolocation API."
                )
            }
            item {
                SolTextField(onContinueClick = {
                    isVerifyingNewApiKeyDialogActive.value = true
                    coroutineScope.launch {
                        isApiKeyValid = try {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = true
                            HTTPClient.ktorClientWithoutCache.get("https://api.ipgeolocation.io/astronomy?apiKey=${newIPGeolocationAPIKey.value}").status.value == 200
                        } catch (_: Exception) {
                            HomeScreenViewModel.Network.isConnectionSucceed.value = false
                            false
                        }
                    }.invokeOnCompletion {
                        if (isApiKeyValid) {
                            coroutineScope.launch {
                                BookMarksVM.dbImplementation.localDBData()
                                    .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                                        this.currentNewsAPIKey = existingNewsAPIKey.value
                                        this.currentNASAAPIKey = existingNasaAPIKey.value
                                        this.currentIPGeoLocationAPIKey =
                                            newIPGeolocationAPIKey.value
                                        this.id = "apiKey"
                                    })
                            }.invokeOnCompletion {
                                coroutineScope.launch {
                                    awaitAll(
                                        async {
                                            newIPGeolocationAPIKey.value =
                                                bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
                                        },
                                        async {
                                            existingIPGeolocationAPIKey.value =
                                                bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
                                        })
                                }.invokeOnCompletion {
                                    isVerifyingNewApiKeyDialogActive.value = false
                                    Toast.makeText(
                                        context,
                                        "Updated IPGeolocation API Key without any failure",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            isVerifyingNewApiKeyDialogActive.value = false
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        newIPGeolocationAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
                                    },
                                    async {
                                        existingIPGeolocationAPIKey.value =
                                            bookMarksVM.getApiKeys()[0].currentIPGeoLocationAPIKey
                                    })
                            }
                            Toast.makeText(
                                context,
                                "Oh-uh, Entered IPGeolocation API Key is invalid:(",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, solValue = newIPGeolocationAPIKey, inSettingsScreen = true)
            }
            item {
                DividerComposable()
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier,
                    title = "Rate Limit",
                    description = "Enter the time interval (in seconds) for updating \"Astronomical Data\" in Home Screen.\ncurrent time interval is : ${Settings.astronomicalTimeInterval.value} seconds."
                )
            }
            item {
                SolTextField(forRateLimit = true, onContinueClick = {
                    coroutineScope.launch {
                        changeAstronomicalTimeIntervalValue(
                            dataStore = dataStore,
                            currentDuration = astronomicalDataFetchingRateLimitValue.value.filter {
                                it.isDigit()
                            }.trim()
                        )
                        readAstronomicalDataTimeIntervalValue(dataStore)
                    }.invokeOnCompletion {
                        Toast.makeText(
                            context,
                            "Astronomical Data will be updated at every ${astronomicalDataFetchingRateLimitValue.value.filter {
                                it.isDigit()
                            }.trim()} seconds.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, solValue = astronomicalDataFetchingRateLimitValue, inSettingsScreen = false)
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
                    }.fillMaxWidth(),
                    title = "Clear Bookmarks",
                    description = "Remove all bookmarks from local database including custom folders. Cache won't be removed."
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
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                            sourceURL = mutableStateOf(
                                "https://github.com/sakethpathike/JetSpacer"
                            )
                        ),
                        {}, inSettingsScreen = true
                    ).fillMaxWidth(),
                    title = "Source code",
                    description = "The code-base for this android client is public and open-source, checkout repository for further information on how this app works and what tech have been used to make this app alive *_*"
                )
            }
            item {
                DividerComposable()
            }
            item {
                Text(
                    text = "Social(s)",
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
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                            sourceURL = mutableStateOf(
                                "https://twitter.com/jetspacerapp"
                            )
                        ),
                        {}, inSettingsScreen = true
                    ).fillMaxWidth(),
                    title = "Twitter",
                    description = "Checkout \"Jet Spacer\" on Twitter for latest updates or anything related to this project."
                )
            }
            item {
                DividerComposable()
            }

            item {
                Text(
                    text = "\uD83D\uDDFF folks",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.titlePadding()
                )
            }

            item {
                Text(
                    text = "Besides code stuff, these are the helping hands that is used by \"Jetspacer\":",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.descriptionPadding(),
                    lineHeight = 16.sp,
                    textAlign= TextAlign.Start
                )
            }
            item {
                IndividualSettingItemComposable(
                    modifier = Modifier.redirectToWeb(
                        navController = navController,
                        newsBottomSheetContentImpl = NewsBottomSheetMutableStateDTO(
                            sourceURL = mutableStateOf(
                                "https://bearblog.dev/"
                            )
                        ),
                        {}, inSettingsScreen = true
                    ).fillMaxWidth(),
                    title = "ʕ•ᴥ•ʔ Bear",
                    description = "Blogging platform without gimmicks."
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .clickable {
                            enableBtmBarInWebView = false
                            if (Settings.inAppBrowserSetting.value) {
                                WebViewUtils.newsBottomSheetContentImpl =
                                    NewsBottomSheetMutableStateDTO(
                                        sourceURL = mutableStateOf(
                                            "https://www.flaticon.com/authors/iconfield"
                                        )
                                    )
                                navController.navigate(NavigationRoutes.WEB_VIEW_SCREEN)
                            } else {
                                localURIHandler.openUri("https://www.flaticon.com/authors/iconfield")
                            }
                        }
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(
                                text = "App Icon by Iconfield",
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.titlePadding(),
                                lineHeight = 20.sp
                            )
                            Text(
                                text = "App icon originally created by Iconfield on Flaticon.",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.descriptionPadding(),
                                lineHeight = 16.sp
                            )
                        }
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
                        }, async {
                            BookMarksVM.dbImplementation.localDBData()
                                .deleteAllDataFromCustomBookMarkDB()
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
            androidx.compose.material3.AlertDialog(
                modifier = Modifier
                    .padding(20.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(5.dp)),
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

        if (isThemeChangeDialogEnabled.value) {
            androidx.compose.material3.AlertDialog(modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = {
                    isThemeChangeDialogEnabled.value = !isThemeChangeDialogEnabled.value
                }) {
                Column {
                    Text(
                        text = "Select theme:",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    themeTypeForThemeDialog.forEach {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        changeThemesValue(
                                            dataStore = dataStore,
                                            currentTheme = it
                                        )
                                        readThemesSetting(dataStore = dataStore)
                                    }
                                    isThemeChangeDialogEnabled.value = false
                                }
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedTheme.value == it,
                                onClick = { selectedTheme.value = it },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.onSurface,
                                    unselectedColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = it,
                                color = if (selectedTheme.value == it) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 15.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
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
        "inAppBrowserChoice"
    ),
    dataStore: DataStore<Preferences>, isInAppBrowsing: Boolean,
) {
    dataStore.edit {
        it[settingsPreferenceKey] = isInAppBrowsing
    }
}

suspend fun changeThemesValue(
    themePreferenceKey: Preferences.Key<String> = preferencesKey(
        "themePreferenceKey"
    ),
    dataStore: DataStore<Preferences>, currentTheme: String,
) {
    dataStore.edit {
        it[themePreferenceKey] = currentTheme
    }
}

suspend fun changeAstronomicalTimeIntervalValue(
    astronomicalTimeInterval: Preferences.Key<String> = preferencesKey(
        "astronomicalTimeInterval"
    ),
    dataStore: DataStore<Preferences>, currentDuration: String,
) {
    dataStore.edit {
        it[astronomicalTimeInterval] = currentDuration
    }
}

suspend fun readInAppBrowserSetting(
    dataStore: DataStore<Preferences>,
    settingsPreferenceKey: Preferences.Key<Boolean> = preferencesKey(
        "inAppBrowserChoice"
    ),
) {
    val preference = dataStore.data.first()[settingsPreferenceKey]
    if (preference != null) {
        Settings.inAppBrowserSetting.value = preference
    }
}

suspend fun readThemesSetting(
    dataStore: DataStore<Preferences>,
    themePreferenceKey: Preferences.Key<String> = preferencesKey(
        "themePreferenceKey"
    ),
) {
    val preference = dataStore.data.first()[themePreferenceKey]
    if (preference != null) {
        Settings.selectedTheme.value = preference
    }
}

suspend fun readAstronomicalDataTimeIntervalValue(
    dataStore: DataStore<Preferences>,
    astronomicalTimeInterval: Preferences.Key<String> = preferencesKey(
        "astronomicalTimeInterval"
    ),
) {
    val preference = dataStore.data.first()[astronomicalTimeInterval]
    if (preference != null) {
        Settings.astronomicalTimeInterval.value = preference
    }
}

object Settings {
    val inAppBrowserSetting = mutableStateOf(true)
    val selectedTheme = mutableStateOf("Follow system theme")
    val astronomicalTimeInterval = mutableStateOf("10")
}

fun Modifier.redirectToWeb(
    navController: NavController,
    newsBottomSheetContentImpl: NewsBottomSheetMutableStateDTO,
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
            localUriHandler.openUri(newsBottomSheetContentImpl.sourceURL.value)
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