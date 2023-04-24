package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.localDB.APIKeysDB
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.navigation.BottomNavigationComposable
import com.sakethh.jetspacer.navigation.MainNavigation
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.settings.readAstronomicalDataTimeIntervalValue
import com.sakethh.jetspacer.screens.settings.readInAppBrowserSetting
import com.sakethh.jetspacer.screens.settings.readThemesSetting
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.*


lateinit var currentDestination: String

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(
        ExperimentalMaterial3Api::class,
        DelicateCoroutinesApi::class,
        ExperimentalMaterialApi::class
    )
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = createDataStore("jetSpacerDataStore")
        lifecycleScope.launch {
            awaitAll(
                async { readInAppBrowserSetting(dataStore) },
                async { readThemesSetting(dataStore) },
                async { readAstronomicalDataTimeIntervalValue(dataStore) })
        }
        GlobalScope.launch {
            HomeScreenViewModel.Network.isConnectedToInternet()
        }
        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            val isConnectedToInternet =
                HomeScreenViewModel.Network.connectedToInternet.collectAsState()
            val bottomSheetState = rememberBottomSheetScaffoldState()
            val bottomBarSheetState = rememberBottomSheetScaffoldState()
            val _currentDestination = navController.currentBackStackEntryAsState()
            currentDestination =
                rememberSaveable(inputs = arrayOf(_currentDestination.value?.destination?.route)) {
                    _currentDestination.value?.destination?.route.toString()
                }
            if (!isConnectedToInternet.value || !HomeScreenViewModel.Network.isConnectionSucceed.value) {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            } else {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.collapse()
                }
            }

            if (currentDestination.toString() == NavigationRoutes.NEWS_SCREEN || currentDestination.toString() == NavigationRoutes.HOME_SCREEN || currentDestination.toString() == NavigationRoutes.SPACE_SCREEN || currentDestination.toString() == NavigationRoutes.BOOKMARKS_SCREEN) {
                coroutineScope.launch {
                    bottomBarSheetState.bottomSheetState.expand()
                }
            } else {
                coroutineScope.launch {
                    bottomBarSheetState.bottomSheetState.collapse()
                }
            }


            AppTheme {
                val systemColor = rememberSystemUiController()
                systemColor.setStatusBarColor(MaterialTheme.colorScheme.surface)
                systemColor.setNavigationBarColor(MaterialTheme.colorScheme.primary)
                Scaffold(modifier = Modifier) {
                    BottomSheetScaffold(sheetPeekHeight = 0.dp,
                        sheetGesturesEnabled = false,
                        drawerBackgroundColor = Color.Transparent,
                        drawerContentColor = Color.Transparent,
                        drawerScrimColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        sheetBackgroundColor = Color.Transparent,
                        scaffoldState = bottomBarSheetState,
                        sheetContent = {
                            BottomNavigationComposable(navController = navController)
                        }) {
                        BottomSheetScaffold(scaffoldState = bottomSheetState,
                            sheetPeekHeight = 0.dp,
                            sheetGesturesEnabled = false,
                            drawerBackgroundColor = Color.Transparent,
                            drawerContentColor = Color.Transparent,
                            drawerScrimColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            sheetBackgroundColor = Color.Transparent,
                            sheetContent = {
                                Snackbar(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .padding(
                                            bottom = 80.dp, start = 20.dp, end = 20.dp
                                        )
                                        .wrapContentHeight()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text(
                                        text = "Network not detected, check your network settings. Once you are connected to a network, swipe down to refresh the data\n",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        softWrap = true,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Start,
                                        lineHeight = 18.sp
                                    )
                                }
                            }) {
                            MainNavigation(
                                navController = navController,
                                dataStore = dataStore
                            )
                        }

                    }
                }
            }
        }
        BookMarksVM.dbImplementation = DBImplementation.getLocalDB(context = this)
        CoroutineScope(Dispatchers.Default).launch {
            if (BookMarksVM.dbImplementation.localDBData().getAPIKeys().isEmpty()) {
                BookMarksVM.dbImplementation.localDBData()
                    .addAPIKeys(apiKeysDB = APIKeysDB().apply {
                        this.id = "apiKey"
                        this.currentNewsAPIKey = Constants.NEWS_API_API_KEY
                        this.currentNASAAPIKey = Constants.NASA_APIKEY
                        this.currentIPGeoLocationAPIKey = Constants.IP_GEOLOCATION_APIKEY
                    })
            }
        }
    }
}